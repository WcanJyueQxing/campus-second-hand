package com.nie.secondhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nie.secondhub.entity.Address;

import java.util.List;

public interface AddressService extends IService<Address> {
    List<Address> getAddressList(Long userId);
    Address getAddressById(Long id, Long userId);
    boolean addAddress(Address address);
    boolean updateAddress(Address address);
    boolean deleteAddress(Long id, Long userId);
    boolean setDefaultAddress(Long id, Long userId);
}