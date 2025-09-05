package com.chromeckap.backend.group;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final GroupValidator groupValidator;


    public GroupResponse getGroupById(final Long id) {
    }

    public Long createGroup(@Valid final GroupRequest request) {
    }

    public Long updateGroup(final Long id, final @Valid GroupRequest request) {
    }

    public void deleteGroup(final Long id) {
    }
}
