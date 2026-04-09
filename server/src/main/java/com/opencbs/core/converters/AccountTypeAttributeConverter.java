package com.opencbs.core.converters;

import com.opencbs.core.domain.enums.AccountType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@SuppressWarnings("unused")
public class AccountTypeAttributeConverter implements AttributeConverter<AccountType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AccountType type) {
        return type.getId();
    }

    @Override
    public AccountType convertToEntityAttribute(Integer s) {
        return AccountType.getById(s);
    }
}
