package alex.valker91.spring_boot.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import alex.valker91.spring_boot.entity.UserAccountDb;
import alex.valker91.spring_boot.model.UserAccount;
import alex.valker91.spring_boot.model.impl.UserAccountImpl;
import alex.valker91.spring_boot.repository.DbUserAccountRepository;
import alex.valker91.spring_boot.service.UserAccountService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Primary
@Service
public class DbUserAccountServiceImpl implements UserAccountService {

    private static final Logger LOGGER = LogManager.getLogger(DbUserAccountServiceImpl.class);

    @Autowired
    private DbUserAccountRepository dbUserAccountRepository;

    @Override
    public int refillUserAccount(long userId, int amount) {
        Optional<UserAccountDb> userAccountDbOptional = dbUserAccountRepository.findById(userId);
        if (userAccountDbOptional.isPresent()) {
            UserAccountDb userAccountDb = userAccountDbOptional.get();
            userAccountDb.setAmount(userAccountDb.getAmount() + amount);
            this.dbUserAccountRepository.save(userAccountDb);
            LOGGER.log(Level.DEBUG, "User with id {} successfully found ", userId);
            return amount;
        } else {
            LOGGER.log(Level.WARN, "Can not to find an user by id: " + userId);
            return -1;
        }
    }

    @Override
    public UserAccount getUserAccountByUserId(long userId) {
        Optional<UserAccountDb> userAccountDbOptional = dbUserAccountRepository.findById(userId);
        if (userAccountDbOptional.isPresent()) {
            UserAccountDb userAccountDb = userAccountDbOptional.get();
            LOGGER.log(Level.DEBUG, "User with id {} successfully found ", userId);
            return mapUserAccountDbToUserAccount(userAccountDb);
        } else {
            LOGGER.log(Level.WARN, "Can not to find an user by id: " + userId);
            return null;
        }
    }

    @Override
    public UserAccount updateUserAccount(UserAccount userAccount) {
        if (userAccount == null) {
            LOGGER.log(Level.WARN, "Can not to update an userAccount: {}", userAccount);
            return null;
        }
        UserAccountDb userAccountDb = mapUserAccountToUserAccountDb(userAccount);
        this.dbUserAccountRepository.save(userAccountDb);
        return userAccount;
    }

    private UserAccountDb mapUserAccountToUserAccountDb(UserAccount userAccount) {
        return new UserAccountDb(userAccount.getId(), userAccount.getUserId(), userAccount.getUserAmount());
    }

    private UserAccount mapUserAccountDbToUserAccount(UserAccountDb userAccountDb) {
        return new UserAccountImpl(userAccountDb.getId(), userAccountDb.getUserId(), userAccountDb.getAmount());
    }
}
