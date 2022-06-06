package com.alkemy.ong.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author nagredo
 * @project OT208-server
 * @class ContactEntity
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contacts")
@Where(clause = "softDelete = false")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE contacts SET softDelete = true WHERE id=?")
public class ContactEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = false, length = 80)
    private String message;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime timestamps;

    @Column(nullable = false)
    private Boolean softDelete = Boolean.FALSE;

}
