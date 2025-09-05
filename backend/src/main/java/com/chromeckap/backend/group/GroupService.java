package com.chromeckap.backend.group;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {
    public GroupResponse getGroupById(Long id) {
    }

    public void createGroup(@Valid GroupRequest request) {
    }

    public void updateGroup(Long id, @Valid GroupRequest request) {
    }

    public void deleteGroup(Long id) {
    }
}
