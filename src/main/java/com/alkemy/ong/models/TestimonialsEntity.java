package com.alkemy.ong.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "testimonials")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE testimonials SET soft_delete = true WHERE id=?")
@Where(clause = "soft_delete = false")
@Schema(description = "This is the model of a testimonials.")
public class TestimonialsEntity implements Serializable {

    private static final long serialVersionUID = 641554778L;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(length = 36)
    @Schema(description = "Id of the testimonials entity.", example = "528f22c3-1f9c-493f-8334-c70b83b5b885",
    	maxLength = 36, minLength = 1)
    private String id;

    @Column(length = 100, nullable = false)
    @Schema(description = "Testimonials name.", example = "My testimonials", maxLength = 100, minLength = 1)
    private String name;

    @Column(length = 255)
    @Schema(description = "Url of the image belonging to the testimonials.",
    	example = "myImage.jpg", maxLength = 255)
    private String image;

    @Column(length = 150, nullable = false)
    @Schema(description = "A content of this testimonials.",
    	example = "This is my testimonials", nullable = true, maxLength = 150)
    private String content;

    @CreatedDate
    @Column(length = 255)
    @Schema(description = "Date the testimonials was created.", example = "21-06-2022 20:24:36")
    private LocalDateTime timestamps;

    @Schema(description = "Indicates if a testimonals is active or not.", example = "false", defaultValue = "false")
    private Boolean softDelete = Boolean.FALSE;
}
