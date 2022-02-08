package com.glanner.api.service;

import com.glanner.api.dto.request.AddGlannerWorkReqDto;
import com.glanner.api.dto.request.AddUserToGlannerReqDto;
import com.glanner.api.dto.request.UpdateGlannerWorkReqDto;
import com.glanner.api.dto.response.FindAttendedGlannerResDto;
import com.glanner.api.dto.response.FindGlannerResDto;
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
import java.util.Optional;

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
    public void saveGlanner(String hostEmail) {

        User findUser = getUser(userRepository.findByEmail(hostEmail));
        Glanner glanner = Glanner.builder()
                .host(findUser)
                .build();

        UserGlanner userGlanner = UserGlanner.builder()
                .user(findUser)
                .build();

        glanner.addUserGlanner(userGlanner);

        Glanner savedGlanner = glannerRepository.save(glanner);
    }

    @Override
    public void deleteGlanner(Long id) {
        Glanner findGlanner = getGlanner(glannerRepository.findById(id));

        glannerRepository.deleteAllWorksById(findGlanner.getId());
        glannerRepository.deleteAllUserGlannerById(findGlanner.getId());
        glannerRepository.delete(findGlanner);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FindAttendedGlannerResDto> findAttendedGlanners(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        return glannerQueryRepository.findAttendedGlannersDtoByUserId(user.getId());
    }

    @Override
    public FindGlannerResDto findGlannerDetail(Long id) {
        Glanner findGlanner = glannerRepository.findRealById(id).orElseThrow(IllegalArgumentException::new);
        List<UserGlanner> findUserGlanners = userGlannerRepository.findByGlannerId(id);
        return new FindGlannerResDto(findGlanner, findUserGlanners);
    }

    @Override
    public void addUser(AddUserToGlannerReqDto reqDto) {

        User attendingUser = getUser(userRepository.findByEmail(reqDto.getEmail()));
        Glanner findGlanner = getGlanner(glannerRepository.findRealById(reqDto.getGlannerId()));
        if (findGlanner.getUserGlanners().size() >= MAX_PERSONNEL_SIZE){
            throw new IllegalStateException("회원 수가 가득 찼습니다.");
        }
        UserGlanner userGlanner = UserGlanner
                .builder()
                .user(attendingUser)
                .build();

        findGlanner.addUserGlanner(userGlanner);
    }

    @Override
    public void deleteUser(Long glannerId, Long userId) {
        Glanner findGlanner = getGlanner(glannerRepository.findById(userId));
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
        Glanner glanner = getGlanner(glannerRepository.findById(reqDto.getGlannerId()));
        glanner.addDailyWork(reqDto.toEntity());
    }

    @Override
    public void deleteDailyWork(Long glanenrId, Long workId) {
        Glanner findGlanner = getGlanner(glannerRepository.findById(glanenrId));
        DailyWorkGlanner deleteWork = dailyWorkGlannerRepository.findById(workId).orElseThrow(IllegalArgumentException::new);
        findGlanner.getWorks().remove(deleteWork);
    }

    @Override
    public void updateDailyWork(UpdateGlannerWorkReqDto reqDto) {
        DailyWorkGlanner updateWork = dailyWorkGlannerRepository.findById(reqDto.getWorkId()).orElseThrow(IllegalArgumentException::new);
        updateWork.changeDailyWork(reqDto.getStartTime(), reqDto.getEndTime(), reqDto.getTitle(), reqDto.getContent());
    }

    public User getUser(Optional<User> user){
        return user.orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
    }

    public Glanner getGlanner(Optional<Glanner> glanner){
        return glanner.orElseThrow(() -> new IllegalArgumentException("글래너가 존재하지 않습니다."));
    }
}
