package com.glanner.api.service;

import com.glanner.api.dto.request.AddGlannerWorkReqDto;
import com.glanner.api.dto.request.AddUserToGlannerReqDto;
import com.glanner.api.dto.request.ChangeGlannerNameReqDto;
import com.glanner.api.dto.request.UpdateGlannerWorkReqDto;
import com.glanner.api.dto.response.FindAttendedGlannerResDto;
import com.glanner.api.dto.response.FindGlannerResDto;
import com.glanner.api.exception.DailyWorkNotFoundException;
import com.glanner.api.exception.FullUserInGroupException;
import com.glanner.api.exception.GlannerNotFoundException;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.api.queryrepository.GlannerQueryRepository;
import com.glanner.core.domain.glanner.DailyWorkGlanner;
import com.glanner.core.domain.glanner.Glanner;
import com.glanner.core.domain.glanner.UserGlanner;
import com.glanner.core.domain.user.User;
import com.glanner.core.repository.DailyWorkGlannerRepository;
import com.glanner.core.repository.GlannerRepository;
import com.glanner.core.repository.UserGlannerRepository;
import com.glanner.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GlannerServiceImpl implements GlannerService{
    private final UserRepository userRepository;
    private final GlannerRepository glannerRepository;
    private final GlannerQueryRepository glannerQueryRepository;
    private final DailyWorkGlannerRepository dailyWorkGlannerRepository;
    private final UserGlannerRepository userGlannerRepository;

    private static final int MAX_PERSONNEL_SIZE = 5;

    @Override
    public Long saveGlanner(String hostEmail) {

        User findUser = userRepository.findByEmail(hostEmail).orElseThrow(UserNotFoundException::new);
        Glanner glanner = Glanner.builder()
                .host(findUser)
                .build();

        UserGlanner userGlanner = UserGlanner.builder()
                .user(findUser)
                .build();

        glanner.addUserGlanner(userGlanner);

        return glannerRepository.save(glanner).getId();
    }

    @Override
    public void deleteGlanner(Long id) {
        Glanner findGlanner = glannerRepository.findById(id).orElseThrow(GlannerNotFoundException::new);

        glannerRepository.deleteAllWorksById(findGlanner.getId());
        glannerRepository.deleteAllUserGlannerById(findGlanner.getId());
        glannerRepository.delete(findGlanner);
    }

    @Override
    public void changeGlannerName(ChangeGlannerNameReqDto reqDto) {
        Glanner glanner = glannerRepository.findById(reqDto.getGlannerId()).orElseThrow(GlannerNotFoundException::new);
        glanner.changeGlannerName(reqDto.getGlannerName());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FindAttendedGlannerResDto> findAttendedGlanners(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        return glannerQueryRepository.findAttendedGlannersDtoByUserId(user.getId());
    }

    @Override
    public FindGlannerResDto findGlannerDetail(Long id) {
        Glanner findGlanner = glannerRepository.findRealById(id).orElseThrow(GlannerNotFoundException::new);
        List<UserGlanner> findUserGlanners = userGlannerRepository.findByGlannerId(id);
        return new FindGlannerResDto(findGlanner, findUserGlanners);
    }

    @Override
    public void addUser(AddUserToGlannerReqDto reqDto) {

        User attendingUser = userRepository.findByEmail(reqDto.getEmail()).orElseThrow(UserNotFoundException::new);
        Glanner findGlanner = glannerRepository.findRealById(reqDto.getGlannerId()).orElseThrow(GlannerNotFoundException::new);
        if (findGlanner.getUserGlanners().size() >= MAX_PERSONNEL_SIZE){
            throw new FullUserInGroupException();
        }
        UserGlanner userGlanner = UserGlanner
                .builder()
                .user(attendingUser)
                .build();

        findGlanner.addUserGlanner(userGlanner);
    }

    @Override
    public void deleteUser(Long glannerId, Long userId) {
        Glanner findGlanner = glannerRepository.findById(userId).orElseThrow(GlannerNotFoundException::new);
        int size = findGlanner.getUserGlanners().size();
        for (int i = 0; i < size; i++) {
            if(findGlanner.getUserGlanners().get(i).getUser().getId().equals(userId)){
                findGlanner.getUserGlanners().get(i).delete();
                break;
            }
        }
    }

    @Override
    public void addDailyWork(AddGlannerWorkReqDto reqDto) {
        Glanner glanner = glannerRepository.findById(reqDto.getGlannerId()).orElseThrow(GlannerNotFoundException::new);
        glanner.addDailyWork(reqDto.toEntity());
    }

    @Override
    public void deleteDailyWork(Long glanenrId, Long workId) {
        Glanner findGlanner = glannerRepository.findById(glanenrId).orElseThrow(GlannerNotFoundException::new);
        DailyWorkGlanner deleteWork = dailyWorkGlannerRepository.findById(workId).orElseThrow(DailyWorkNotFoundException::new);
        findGlanner.getWorks().remove(deleteWork);
    }

    @Override
    public void updateDailyWork(UpdateGlannerWorkReqDto reqDto) {
        DailyWorkGlanner updateWork = dailyWorkGlannerRepository.findById(reqDto.getWorkId()).orElseThrow(DailyWorkNotFoundException::new);
        updateWork.changeDailyWork(reqDto.getStartTime(), reqDto.getEndTime(), reqDto.getTitle(), reqDto.getContent());
    }
}
