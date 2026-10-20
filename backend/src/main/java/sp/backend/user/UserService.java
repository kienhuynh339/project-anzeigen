package sp.backend.user;

import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository){this.userRepository=userRepository;}

    public UserPublicDto findByIdPublic(Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return new UserPublicDto(user.get());
        }else{
            return null;
        }
    }

    public UserPrivateDto findByIdPrivate(Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return new UserPrivateDto(user.get());
        }else{
            return null;
        }
    }
}
