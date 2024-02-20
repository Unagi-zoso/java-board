package com.nightdiver.javaboard.repository;

import com.nightdiver.javaboard.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
}
