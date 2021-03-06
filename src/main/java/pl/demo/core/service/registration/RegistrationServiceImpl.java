package pl.demo.core.service.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.core.model.entity.RoleName;
import pl.demo.core.model.entity.User;
import pl.demo.core.model.repo.RoleRepository;
import pl.demo.core.model.repo.UserRepository;
import pl.demo.core.service.basicservice.CRUDServiceImpl;
import pl.demo.core.service.mail.MailDTOSupplier;
import pl.demo.core.service.mail.TemplateType;
import pl.demo.core.service.mail.event.SendMailEvent;


/**
 * Created by robertsikora on 05.11.2015.
 */


public class RegistrationServiceImpl extends CRUDServiceImpl<Long, User> implements RegistrationService {

    private final static String EMAIL_TITLE = "Zarejstrowano nowego usera";
    private final static String EMAIL_CONTENT = "registration";

    private UserRepository      userRepository;
    private RoleRepository      roleRepository;
    private PasswordEncoder     passwordEncoder;

    @Transactional
    @Override
    public User registerAccount(final User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.addRole(this.roleRepository.findByRoleName(RoleName.USER_ROLE));
        user.setAccountStatus(AccountStatus.ACTIVE);
        User registered = userRepository.save(user);
        publishBusinessEvent(new SendMailEvent(MailDTOSupplier.get(EMAIL_TITLE, EMAIL_CONTENT).get(),
                TemplateType.REGISTRATION_TEMPLATE));
        return registered;
    }

    @Override
    public void activateAccount(Long userId, String activationCode) {
        throw new UnsupportedOperationException("Not implemented yet !");
    }

    @Override
    public void deactivateAccount(Long userId) {
        throw new UnsupportedOperationException("Not implemented yet !");
    }

    @Autowired
    public void setRoleRepository(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setPasswordEncoder(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserRepository(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
