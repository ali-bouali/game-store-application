package com.alibou.store.gamerequest;


import com.alibou.store.common.BaseEntity;
import com.alibou.store.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GameRequestEntity extends BaseEntity {

    private String title;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
