package com.nie.secondhub.controller.user;

import com.nie.secondhub.common.context.LoginUserHolder;
import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.entity.Address;
import com.nie.secondhub.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/list")
    public ApiResponse<List<Address>> list() {
        Long userId = LoginUserHolder.requireUserId();
        List<Address> list = addressService.getAddressList(userId);
        return ApiResponse.success(list);
    }

    @GetMapping("/{id}")
    public ApiResponse<Address> getById(@PathVariable Long id) {
        Long userId = LoginUserHolder.requireUserId();
        Address address = addressService.getAddressById(id, userId);
        return ApiResponse.success(address);
    }

    @PostMapping
    public ApiResponse<Void> add(@RequestBody Address address) {
        Long userId = LoginUserHolder.requireUserId();
        address.setUserId(userId);
        addressService.addAddress(address);
        return ApiResponse.success(null);
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody Address address) {
        Long userId = LoginUserHolder.requireUserId();
        address.setId(id);
        address.setUserId(userId);
        addressService.updateAddress(address);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        Long userId = LoginUserHolder.requireUserId();
        addressService.deleteAddress(id, userId);
        return ApiResponse.success(null);
    }

    @PutMapping("/{id}/default")
    public ApiResponse<Void> setDefault(@PathVariable Long id) {
        Long userId = LoginUserHolder.requireUserId();
        addressService.setDefaultAddress(id, userId);
        return ApiResponse.success(null);
    }
}