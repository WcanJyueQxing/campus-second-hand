package com.nie.secondhub.controller.user;

import com.nie.secondhub.common.context.LoginUserHolder;
import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.entity.Address;
import com.nie.secondhub.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址管理控制器
 * 提供收货地址的增删改查接口
 */
@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * 获取当前用户的地址列表
     * 
     * @return 地址列表
     */
    @GetMapping("/list")
    public ApiResponse<List<Address>> list() {
        Long userId = LoginUserHolder.requireUserId();
        List<Address> list = addressService.getAddressList(userId);
        return ApiResponse.success(list);
    }

    /**
     * 获取单个地址详情
     * 
     * @param id 地址ID
     * @return 地址详情
     */
    @GetMapping("/{id}")
    public ApiResponse<Address> getById(@PathVariable Long id) {
        Long userId = LoginUserHolder.requireUserId();
        Address address = addressService.getAddressById(id, userId);
        return ApiResponse.success(address);
    }

    /**
     * 添加新地址
     * 
     * @param address 地址信息
     * @return 操作结果
     */
    @PostMapping
    public ApiResponse<Void> add(@RequestBody Address address) {
        Long userId = LoginUserHolder.requireUserId();
        address.setUserId(userId);
        addressService.addAddress(address);
        return ApiResponse.success(null);
    }

    /**
     * 更新地址
     * 
     * @param id 地址ID
     * @param address 地址信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody Address address) {
        Long userId = LoginUserHolder.requireUserId();
        address.setId(id);
        address.setUserId(userId);
        addressService.updateAddress(address);
        return ApiResponse.success(null);
    }

    /**
     * 删除地址
     * 
     * @param id 地址ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        Long userId = LoginUserHolder.requireUserId();
        addressService.deleteAddress(id, userId);
        return ApiResponse.success(null);
    }

    /**
     * 设置默认地址
     * 
     * @param id 地址ID
     * @return 操作结果
     */
    @PutMapping("/{id}/default")
    public ApiResponse<Void> setDefault(@PathVariable Long id) {
        Long userId = LoginUserHolder.requireUserId();
        addressService.setDefaultAddress(id, userId);
        return ApiResponse.success(null);
    }
}