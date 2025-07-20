package com.chromeckap.backend.group;

import com.chromeckap.backend.category.Category;
import com.chromeckap.backend.common.AuditingEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "groups")
public class Group extends AuditingEntity {

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String inviteCode;

    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Category> categories = new ArrayList<>();

}
