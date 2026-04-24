package com.nie.secondhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nie.secondhub.entity.Address;
import com.nie.secondhub.mapper.AddressMapper;
import com.nie.secondhub.service.AddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Override
    public List<Address> getAddressList(Long userId) {
        QueryWrapper<Address> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("is_deleted", 0);
        wrapper.orderByDesc("is_default").orderByDesc("created_at");
        return list(wrapper);
    }

    @Override
    public Address getAddressById(Long id, Long userId) {
        QueryWrapper<Address> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        wrapper.eq("user_id", userId);
        wrapper.eq("is_deleted", 0);
        return getOne(wrapper);
    }

    @Override
    public boolean addAddress(Address address) {
        address.setCreatedAt(LocalDateTime.now());
        address.setUpdatedAt(LocalDateTime.now());
        address.setIsDeleted(0);
        if (address.getIsDefault() == null || address.getIsDefault() == 0) {
            address.setIsDefault(0);
        } else {
            clearDefaultAddress(address.getUserId());
        }
        return save(address);
    }

    @Override
    public boolean updateAddress(Address address) {
        address.setUpdatedAt(LocalDateTime.now());
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            clearDefaultAddress(address.getUserId());
        }
        QueryWrapper<Address> wrapper = new QueryWrapper<>();
        wrapper.eq("id", address.getId());
        wrapper.eq("user_id", address.getUserId());
        return update(address, wrapper);
    }

    @Override
    @Transactional
    public boolean deleteAddress(Long id, Long userId) {
        QueryWrapper<Address> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        wrapper.eq("user_id", userId);
        Address address = new Address();
        address.setIsDeleted(1);
        address.setUpdatedAt(LocalDateTime.now());
        return update(address, wrapper);
    }

    @Override
    @Transactional
    public boolean setDefaultAddress(Long id, Long userId) {
        clearDefaultAddress(userId);
        QueryWrapper<Address> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        wrapper.eq("user_id", userId);
        Address address = new Address();
        address.setIsDefault(1);
        address.setUpdatedAt(LocalDateTime.now());
        return update(address, wrapper);
    }

    private void clearDefaultAddress(Long userId) {
        QueryWrapper<Address> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("is_default", 1);
        wrapper.eq("is_deleted", 0);
        Address address = new Address();
        address.setIsDefault(0);
        address.setUpdatedAt(LocalDateTime.now());
        update(address, wrapper);
    }
}