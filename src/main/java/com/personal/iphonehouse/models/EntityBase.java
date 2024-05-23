package com.personal.iphonehouse.models;

import com.personal.iphonehouse.utils.DateUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class EntityBase<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private T id;
    private boolean isDelete;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date dateCreated;
    @LastModifiedDate
    private Date dateUpdated;

    @PrePersist
    void initializeDate() {
        dateCreated = new DateUtil().utilDateNow();
    }
}
