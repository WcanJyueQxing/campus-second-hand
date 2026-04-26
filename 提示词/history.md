# 历史浏览功能实现文档

## 1. 功能概述

历史浏览功能允许用户查看自己浏览过的商品记录，支持以下核心功能：

- **自动记录**：用户浏览商品详情时自动添加到历史记录
- **按日期分组**：前端展示时按浏览日期分组
- **查看历史**：查看历史浏览记录列表
- **删除记录**：删除单条历史记录
- **清空记录**：清空所有历史浏览记录
- **快速跳转**：从历史记录跳转到商品详情页

## 2. 技术实现

### 2.1 前端实现

#### 核心文件
- **`second-hub-client/pages/history/history.js`** - 历史浏览页面逻辑
- **`second-hub-client/pages/history/history.json`** - 页面配置
- **`second-hub-client/pages/goods-detail/goods-detail.js`** - 添加历史记录的逻辑

#### 关键功能

1. **添加历史记录**（在商品详情页）
   ```javascript
   // 添加到历史浏览记录
   addToHistory(goods) {
     if (!goods || !goods.id) return
     
     const historyData = {
       goodsId: goods.id,
       title: goods.title,
       price: goods.price,
       images: goods.images || []
     }
     request({ url: '/api/user/history', method: 'POST', data: historyData }).then(() => {
       console.log('历史记录添加成功')
     }).catch(() => {
       console.log('历史记录添加失败')
     })
   }
   ```

2. **加载历史记录**
   ```javascript
   // 加载历史浏览记录
   loadHistory() {
     wx.showLoading({ title: '加载中...' })
     request({ url: '/api/user/history' }).then((data) => {
       const historyList = data || []
       // 按浏览时间倒序排序
       historyList.sort((a, b) => new Date(b.viewTime) - new Date(a.viewTime))
       // 按日期分组
       const groupedHistory = this.groupByDate(historyList)
       this.setData({ historyList, groupedHistory })
     }).finally(() => {
       wx.hideLoading()
     })
   }
   ```

3. **按日期分组**
   ```javascript
   // 按日期分组
   groupByDate(historyList) {
     const groups = {}
     
     historyList.forEach(item => {
       const date = this.formatDate(item.viewTime)
       if (!groups[date]) {
         groups[date] = []
       }
       groups[date].push(item)
     })
     
     // 转换为数组并按日期倒序排序
     return Object.keys(groups).map(date => ({
       date,
       items: groups[date]
     })).sort((a, b) => {
       // 按日期倒序排序
       return new Date(b.date) - new Date(a.date)
     })
   }
   ```

4. **删除单条记录**
   ```javascript
   // 删除单条历史记录
   deleteHistory(e) {
     const { goodsId } = e.currentTarget.dataset
     wx.showLoading({ title: '删除中...' })
     request({ url: `/api/user/history/${goodsId}`, method: 'DELETE' }).then(() => {
       this.loadHistory()
       wx.showToast({ title: '删除成功', icon: 'success' })
     }).finally(() => {
       wx.hideLoading()
     })
   }
   ```

5. **清空所有记录**
   ```javascript
   // 清空历史记录
   clearHistory() {
     wx.showModal({
       title: '确认清空',
       content: '确定要清空所有历史浏览记录吗？',
       success: (res) => {
         if (res.confirm) {
           wx.showLoading({ title: '清空ing...' })
           request({ url: '/api/user/history', method: 'DELETE' }).then(() => {
             this.loadHistory()
             wx.showToast({ title: '清空成功', icon: 'success' })
           }).finally(() => {
             wx.hideLoading()
           })
         }
       }
     })
   }
   ```

### 2.2 后端实现

#### 核心文件
- **`second-hub-server/src/main/java/com/nie/secondhub/controller/user/HistoryRecordController.java`** - 历史记录API接口
- **`second-hub-server/src/main/java/com/nie/secondhub/service/HistoryRecordService.java`** - 历史记录服务接口
- **`second-hub-server/src/main/java/com/nie/secondhub/service/impl/HistoryRecordServiceImpl.java`** - 历史记录服务实现
- **`second-hub-server/src/main/java/com/nie/secondhub/entity/HistoryRecord.java`** - 历史记录实体类
- **`second-hub-server/src/main/java/com/nie/secondhub/dto/user/HistoryRecordRequest.java`** - 添加历史记录的请求DTO
- **`second-hub-server/src/main/java/com/nie/secondhub/vo/HistoryRecordVO.java`** - 返回给前端的VO
- **`second-hub-server/src/main/java/com/nie/secondhub/mapper/HistoryRecordMapper.java`** - 历史记录Mapper
- **`second-hub-server/src/main/resources/mapper/HistoryRecordMapper.xml`** - 历史记录Mapper XML配置

#### 数据库表结构

**`history_record`表**
| 字段名 | 数据类型 | 说明 |
|--------|----------|------|
| `id` | `BIGINT` | 主键ID |
| `user_id` | `BIGINT` | 用户ID |
| `goods_id` | `BIGINT` | 商品ID |
| `title` | `VARCHAR(255)` | 商品标题 |
| `price` | `DOUBLE` | 商品价格 |
| `images` | `TEXT` | 商品图片（JSON字符串） |
| `view_time` | `DATETIME` | 浏览时间 |
| `created_at` | `DATETIME` | 创建时间 |
| `updated_at` | `DATETIME` | 更新时间 |

#### API接口

| 接口 | 方法 | 路径 | 功能 |
|------|------|------|------|
| 添加历史记录 | `POST` | `/api/user/history` | 添加商品浏览记录 |
| 获取历史记录 | `GET` | `/api/user/history` | 获取用户历史浏览记录列表 |
| 删除单条记录 | `DELETE` | `/api/user/history/{goodsId}` | 删除指定商品的历史记录 |
| 清空历史记录 | `DELETE` | `/api/user/history` | 清空用户所有历史记录 |

#### 关键业务逻辑

1. **添加历史记录**
   ```java
   @Override
   public void addHistoryRecord(Long userId, HistoryRecordRequest request) {
       // 先删除已存在的记录，避免重复
       historyRecordMapper.deleteByUserIdAndGoodsId(userId, request.getGoodsId());

       // 创建新的历史记录
       HistoryRecord record = new HistoryRecord();
       record.setUserId(userId);
       record.setGoodsId(request.getGoodsId());
       record.setTitle(request.getTitle());
       record.setPrice(request.getPrice());
       try {
           record.setImages(objectMapper.writeValueAsString(request.getImages()));
       } catch (JsonProcessingException e) {
           record.setImages("[]");
       }
       record.setViewTime(LocalDateTime.now());
       record.setCreatedAt(LocalDateTime.now());
       record.setUpdatedAt(LocalDateTime.now());

       historyRecordMapper.insert(record);
   }
   ```

2. **获取历史记录**
   ```java
   @Override
   public List<HistoryRecordVO> getHistoryRecords(Long userId, Integer limit) {
       if (limit == null || limit <= 0) {
           limit = 50;
       }
       List<HistoryRecord> records = historyRecordMapper.selectByUserIdOrderByViewTimeDesc(userId, limit);
       return records.stream().map(this::convertToVO).collect(Collectors.toList());
   }
   ```

3. **转换为VO**
   ```java
   private HistoryRecordVO convertToVO(HistoryRecord record) {
       HistoryRecordVO vo = new HistoryRecordVO();
       vo.setId(record.getId());
       vo.setGoodsId(record.getGoodsId());
       vo.setTitle(record.getTitle());
       vo.setPrice(record.getPrice());
       try {
           vo.setImages(objectMapper.readValue(record.getImages(), String[].class));
       } catch (JsonProcessingException e) {
           vo.setImages(new String[0]);
       }
       vo.setViewTime(record.getViewTime());
       return vo;
   }
   ```

4. **删除单条记录**
   ```java
   @Override
   public void deleteHistoryRecord(Long userId, Long goodsId) {
       historyRecordMapper.deleteByUserIdAndGoodsId(userId, goodsId);
   }
   ```

5. **清空历史记录**
   ```java
   @Override
   public void clearHistoryRecords(Long userId) {
       historyRecordMapper.deleteByUserId(userId);
   }
   ```

## 3. 数据流程

1. **添加历史记录流程**
   - 用户访问商品详情页
   - 前端调用`addToHistory`方法
   - 发送POST请求到`/api/user/history`
   - 后端先删除已存在的同商品记录
   - 创建新的历史记录并保存到数据库

2. **查看历史记录流程**
   - 用户进入历史浏览页面
   - 前端调用`loadHistory`方法
   - 发送GET请求到`/api/user/history`
   - 后端查询用户的历史记录，按浏览时间倒序
   - 前端接收数据并按日期分组展示

3. **删除历史记录流程**
   - 用户点击删除按钮
   - 前端调用`deleteHistory`方法
   - 发送DELETE请求到`/api/user/history/{goodsId}`
   - 后端删除指定商品的历史记录
   - 前端重新加载历史记录

4. **清空历史记录流程**
   - 用户点击清空按钮
   - 前端显示确认弹窗
   - 用户确认后发送DELETE请求到`/api/user/history`
   - 后端删除用户所有历史记录
   - 前端重新加载历史记录

## 4. 技术特点

1. **去重机制**：添加历史记录时会先删除已存在的同商品记录，确保每条商品只保留最新的浏览记录

2. **日期分组**：前端实现了按日期分组展示，提高用户体验

3. **图片处理**：使用JSON序列化和反序列化处理商品图片数组

4. **错误处理**：对JSON处理异常进行了捕获，确保系统稳定性

5. **性能优化**：
   - 限制返回记录数量，默认50条
   - 按浏览时间倒序查询，确保最新的记录优先展示

6. **用户体验**：
   - 加载时显示loading状态
   - 删除操作后显示成功提示
   - 清空操作前显示确认弹窗

## 5. 代码优化建议

1. **缓存机制**：可以考虑使用Redis缓存历史记录，减少数据库查询压力

2. **批量操作**：支持批量删除历史记录，提高用户操作效率

3. **分页查询**：实现分页加载历史记录，避免一次性加载过多数据

4. **数据清理**：添加定时任务清理过期的历史记录，保持数据库清洁

5. **用户设置**：允许用户设置历史记录保存时间和数量限制

## 6. 总结

历史浏览功能是一个重要的用户体验功能，它允许用户方便地回顾自己浏览过的商品。本实现通过前后端配合，实现了完整的历史记录管理功能，包括自动记录、按日期分组展示、删除和清空操作等。

系统设计合理，代码结构清晰，具有良好的可扩展性和维护性。通过去重机制和性能优化，确保了功能的流畅运行。

该功能的实现为用户提供了便捷的商品回顾方式，有助于提高用户的购物体验和平台的用户粘性。