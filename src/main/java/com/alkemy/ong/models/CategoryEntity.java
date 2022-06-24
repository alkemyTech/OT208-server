package com.alkemy.ong.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE categories SET soft_delete = true WHERE id=?")
@Where(clause = "soft_delete = false")
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "This is the model of a category.")
public class CategoryEntity implements Serializable {

    private static final long serialVersionUID = 741558L;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(length = 36)
    @Schema(description = "Id of the category entity.", example = "528f22c3-1f9c-493f-8334-c70b83b5b885",
    		maxLength = 50, minLength = 1)
    private String id;

    @Column(nullable = false, length = 50)
    @Schema(description = "Category name.", example = "Drama", maxLength = 50, minLength = 1)
    private String name;

    @Schema(description = "A description of what this category represents.", 
    		example = "This is a melodramatic type category", nullable = true)
    private String description;

    @Column(nullable = false, length = 255)
    @Schema(description = "Url of the image belonging to the category.", 
    		example = "myImage.jpg", maxLength = 255, minLength = 1)
    private String image;

    @Column(nullable = false)
    @CreatedDate
    @Schema(description = "Date the category was created.", example = "21-06-2022 20:24:36")
    private LocalDateTime timestamps;

    @Schema(description = "Indicates if a category is active or not.", example = "false", defaultValue = "false")
    private Boolean softDelete = Boolean.FALSE;

}
