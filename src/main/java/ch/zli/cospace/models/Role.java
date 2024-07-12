package ch.zli.cospace.models;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum Role {
    @FieldNameConstants.Include MEMBER,
    @FieldNameConstants.Include ADMIN
}
