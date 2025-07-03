package com.obaidullah.cms.auth.repositories;

import com.obaidullah.cms.auth.entities.ForgotPassword;
import com.obaidullah.cms.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;
@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {

    @Query("select fp from ForgotPassword fp where fp.otp = ?1 and fp.user = ?2")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);
    Optional<ForgotPassword> findByUser(User user);
    @Modifying
    @Query("DELETE FROM ForgotPassword fp WHERE fp.expirationTime < :currentDate")
    void deleteAllByExpirationTimeBefore(@Param("currentDate") Date currentDate);
}
