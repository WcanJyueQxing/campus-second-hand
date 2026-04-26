商品收藏功能实现文档

## 1. 功能概述

商品收藏功能允许用户收藏心仪的商品，方便后续快速查看，支持以下核心功能：

- **添加收藏**：用户在商品详情页可收藏商品，重复收藏做幂等处理
- **取消收藏**：取消单条已收藏的商品记录
- **查看收藏**：查看收藏的商品列表，支持按收藏时间排序
- **批量操作**：批量取消收藏指定商品
- **快速跳转**：从收藏列表跳转到商品详情页

## 2. 技术实现

### 2.1 前端实现

#### 核心文件

- **`second-hub-client/pages/collection/collection.js`** - 商品收藏页面逻辑
- **`second-hub-client/pages/collection/collection.json`** - 页面配置
- **`second-hub-client/pages/goods-detail/goods-detail.js`** - 添加 / 取消收藏的逻辑

#### 关键功能

1. **添加收藏**（在商品详情页）

   1. ```javascript
      // 添加商品收藏
      addToCollection(goods) {
        if (!goods || !goods.id) return
        
        const collectionData = {
          goodsId: goods.id,
          title: goods.title,
          price: goods.price,
          images: goods.images || []
        }
        wx.showLoading({ title: '收藏中...' })
        request({ url: '/api/user/collection', method: 'POST', data: collectionData }).then(() => {
          wx.showToast({ title: '收藏成功', icon: 'success' })
          // 更新页面收藏状态
          this.setData({ isCollected: true })
        }).catch(() => {
          wx.showToast({ title: '收藏失败', icon: 'none' })
        }).finally(() => {
          wx.hideLoading()
        })
      }
      ```

2. **取消单条收藏**

   1. ```javascript
      // 取消单条商品收藏
      cancelCollection(e) {
        const { goodsId } = e.currentTarget.dataset
        wx.showLoading({ title: '取消中...' })
        request({ url: `/api/user/collection/${goodsId}`, method: 'DELETE' }).then(() => {
          this.loadCollection()
          wx.showToast({ title: '取消收藏成功', icon: 'success' })
        }).finally(() => {
          wx.hideLoading()
        })
      }
      ```

3. **加载收藏列表**

   1. ```javascript
      // 加载商品收藏列表
      loadCollection() {
        wx.showLoading({ title: '加载中...' })
        request({ url: '/api/user/collection' }).then((data) => {
          const collectionList = data || []
          // 按收藏时间倒序排序
          collectionList.sort((a, b) => new Date(b.collectTime) - new Date(a.collectTime))
          this.setData({ collectionList })
        }).finally(() => {
          wx.hideLoading()
        })
      }
      ```

4. **批量取消收藏**

   1. ```javascript
      // 批量取消收藏
      batchCancelCollection() {
        const { selectedGoodsIds } = this.data
        if (!selectedGoodsIds || selectedGoodsIds.length === 0) {
          wx.showToast({ title: '请选择要取消收藏的商品', icon: 'none' })
          return
        }
        
        wx.showModal({
          title: '确认批量取消',
          content: `确定要取消选中的${selectedGoodsIds.length}个商品收藏吗？`,
          success: (res) => {
            if (res.confirm) {
              wx.showLoading({ title: '处理中...' })
              request({ 
                url: '/api/user/collection/batch', 
                method: 'DELETE', 
                data: { goodsIds: selectedGoodsIds } 
              }).then(() => {
                this.loadCollection()
                // 清空选中状态
                this.setData({ selectedGoodsIds: [] })
                wx.showToast({ title: '批量取消成功', icon: 'success' })
              }).finally(() => {
                wx.hideLoading()
              })
            }
          }
        })
      }
      ```

5. **检查商品收藏状态**（商品详情页）

   1. ```javascript
      // 检查商品是否已收藏
      checkCollectionStatus(goodsId) {
        request({ url: `/api/user/collection/check/${goodsId}` }).then((data) => {
          this.setData({ isCollected: data || false })
        }).catch(() => {
          this.setData({ isCollected: false })
        })
      }
      ```

### 2.2 后端实现

#### 核心文件

- **`second-hub-server/src/main/java/com/nie/secondhub/controller/user/CollectionController.java`** - 收藏功能 API 接口
- **`second-hub-server/src/main/java/com/nie/secondhub/service/CollectionService.java`** - 收藏服务接口
- **`second-hub-server/src/main/java/com/nie/secondhub/service/impl/CollectionServiceImpl.java`** - 收藏服务实现
- **`second-hub-server/src/main/java/com/nie/secondhub/entity/Collection.java`** - 收藏实体类
- **`second-hub-server/src/main/java/com/nie/secondhub/dto/user/CollectionRequest.java`** - 添加收藏的请求 DTO
- **`second-hub-server/src/main/java/com/nie/secondhub/vo/CollectionVO.java`** - 返回给前端的 VO
- **`second-hub-server/src/main/java/com/nie/secondhub/mapper/CollectionMapper.java`** - 收藏 Mapper
- **`second-hub-server/src/main/resources/mapper/CollectionMapper.xml`** - 收藏 Mapper XML 配置

#### 数据库表结构

**`collection`****表**

| 字段名         | 数据类型       | 说明                                   |
| -------------- | -------------- | -------------------------------------- |
| `id`           | `BIGINT`       | 主键 ID                                |
| `user_id`      | `BIGINT`       | 用户 ID                                |
| `goods_id`     | `BIGINT`       | 商品 ID                                |
| `title`        | `VARCHAR(255)` | 商品标题                               |
| `price`        | `DOUBLE`       | 商品价格                               |
| `images`       | `TEXT`         | 商品图片（JSON 字符串）                |
| `collect_time` | `DATETIME`     | 收藏时间                               |
| `created_at`   | `DATETIME`     | 创建时间                               |
| `updated_at`   | `DATETIME`     | 更新时间                               |
| `is_deleted`   | `TINYINT`      | 逻辑删除标识（0 - 未删除，1 - 已删除） |

#### API 接口

| 接口         | 方法     | 路径                                   | 功能                   |
| ------------ | -------- | -------------------------------------- | ---------------------- |
| 添加收藏     | `POST`   | `/api/user/collection`                 | 添加商品收藏（幂等）   |
| 获取收藏列表 | `GET`    | `/api/user/collection`                 | 获取用户收藏的商品列表 |
| 取消单条收藏 | `DELETE` | `/api/user/collection/{goodsId}`       | 取消指定商品的收藏     |
| 批量取消收藏 | `DELETE` | `/api/user/collection/batch`           | 批量取消选中商品的收藏 |
| 检查收藏状态 | `GET`    | `/api/user/collection/check/{goodsId}` | 检查指定商品是否已收藏 |

#### 关键业务逻辑

1. **添加收藏**

   1. ```java
      @Override
      public void addCollection(Long userId, CollectionRequest request) {
          // 幂等处理：先查询是否已收藏，已收藏则更新时间，未收藏则新增
          Collection existing = collectionMapper.selectByUserIdAndGoodsId(userId, request.getGoodsId());
          if (existing != null) {
              existing.setCollectTime(LocalDateTime.now());
              existing.setUpdatedAt(LocalDateTime.now());
              collectionMapper.updateById(existing);
              return;
          }
      
          // 创建新的收藏记录
          Collection collection = new Collection();
          collection.setUserId(userId);
          collection.setGoodsId(request.getGoodsId());
          collection.setTitle(request.getTitle());
          collection.setPrice(request.getPrice());
          try {
              collection.setImages(objectMapper.writeValueAsString(request.getImages()));
          } catch (JsonProcessingException e) {
              collection.setImages("[]");
          }
          collection.setCollectTime(LocalDateTime.now());
          collection.setCreatedAt(LocalDateTime.now());
          collection.setUpdatedAt(LocalDateTime.now());
          collection.setIsDeleted(0);
      
          collectionMapper.insert(collection);
      }
      ```

2. **获取收藏列表**

   1. ```java
      @Override
      public List<CollectionVO> getCollections(Long userId, Integer limit) {
          if (limit == null || limit <= 0) {
              limit = 50;
          }
          // 查询未删除的收藏记录，按收藏时间倒序
          List<Collection> collections = collectionMapper.selectByUserIdAndNotDeletedOrderByCollectTimeDesc(userId, limit);
          return collections.stream().map(this::convertToVO).collect(Collectors.toList());
      }
      ```

3. **转换为 VO**

   1. ```java
      private CollectionVO convertToVO(Collection collection) {
          CollectionVO vo = new CollectionVO();
          vo.setId(collection.getId());
          vo.setGoodsId(collection.getGoodsId());
          vo.setTitle(collection.getTitle());
          vo.setPrice(collection.getPrice());
          try {
              vo.setImages(objectMapper.readValue(collection.getImages(), String[].class));
          } catch (JsonProcessingException e) {
              vo.setImages(new String[0]);
          }
          vo.setCollectTime(collection.getCollectTime());
          return vo;
      }
      ```

4. **取消单条收藏**（逻辑删除）

   1. ```java
      @Override
      public void cancelCollection(Long userId, Long goodsId) {
          Collection collection = collectionMapper.selectByUserIdAndGoodsId(userId, goodsId);
          if (collection != null) {
              collection.setIsDeleted(1);
              collection.setUpdatedAt(LocalDateTime.now());
              collectionMapper.updateById(collection);
          }
      }
      ```

5. **批量取消收藏**

   1. ```java
      @Override
      public void batchCancelCollection(Long userId, List<Long> goodsIds) {
          if (CollectionUtils.isEmpty(goodsIds)) {
              return;
          }
          collectionMapper.batchUpdateIsDeletedByUserIdAndGoodsIds(userId, goodsIds, 1, LocalDateTime.now());
      }
      ```

6. **检查收藏状态**

   1. ```java
      @Override
      public Boolean checkCollectionStatus(Long userId, Long goodsId) {
          Collection collection = collectionMapper.selectByUserIdAndGoodsId(userId, goodsId);
          return collection != null && collection.getIsDeleted() == 0;
      }
      ```

## 3. 数据流程

1. **添加收藏流程**
   1. 用户访问商品详情页，点击收藏按钮
   2. 前端调用`addToCollection`方法，携带商品信息发送 POST 请求
   3. 后端检查该商品是否已收藏：已收藏则更新收藏时间，未收藏则新增记录
   4. 前端接收响应，更新页面收藏状态并提示用户
2. **查看收藏列表流程**
   1. 用户进入商品收藏页面
   2. 前端调用`loadCollection`方法，发送 GET 请求
   3. 后端查询用户未删除的收藏记录，按收藏时间倒序返回
   4. 前端接收数据并渲染列表
3. **取消单条收藏流程**
   1. 用户在收藏列表点击取消收藏按钮
   2. 前端调用`cancelCollection`方法，发送 DELETE 请求
   3. 后端将该收藏记录标记为逻辑删除
   4. 前端重新加载收藏列表并提示用户
4. **批量取消收藏流程**
   1. 用户在收藏列表选中多个商品，点击批量取消按钮
   2. 前端弹出确认弹窗，用户确认后发送批量取消请求
   3. 后端批量更新选中商品的收藏记录为逻辑删除
   4. 前端清空选中状态、重新加载列表并提示用户
5. **检查收藏状态流程**
   1. 用户进入商品详情页
   2. 前端调用`checkCollectionStatus`方法，发送 GET 请求
   3. 后端查询并返回该商品的收藏状态
   4. 前端根据状态展示 “已收藏” 或 “收藏” 按钮

## 4. 技术特点

1. **幂等处理**：添加收藏时做幂等校验，避免重复创建记录，已收藏则更新收藏时间
2. **逻辑删除**：取消收藏采用逻辑删除，保留历史数据，便于数据复盘和恢复
3. **批量操作**：支持批量取消收藏，提升用户操作效率
4. **状态校验**：商品详情页实时校验收藏状态，保证展示准确性
5. **性能优化**：
   1. 限制返回记录数量，默认 50 条，避免数据过多
   2. 按收藏时间倒序查询，最新收藏的商品优先展示
6. **用户体验**：
   1. 操作过程中显示 loading 状态，提升感知
   2. 操作完成后给出明确的 Toast 提示
   3. 批量操作前增加确认弹窗，防止误操作

## 5. 代码优化建议

1. **缓存优化**：使用 Redis 缓存用户收藏状态和收藏列表，减少数据库查询压力，设置合理的过期时间
2. **实时同步**：商品信息变更（如价格、标题）时，同步更新收藏记录中的商品信息，保证数据一致性
3. **分页查询**：实现分页加载收藏列表，支持下拉加载更多，优化大数据量下的加载性能
4. **收藏分类**：支持用户对收藏商品进行分类管理（如 “想要”、“比价” 等），提升管理效率
5. **过期清理**：添加定时任务，清理长期未操作的无效收藏记录（如商品已下架的收藏）
6. **推送提醒**：收藏商品降价、下架时，推送消息提醒用户，提升功能价值

## 6. 总结

商品收藏功能是提升用户体验的核心功能之一，能够帮助用户快速留存心仪商品，降低复购决策成本。本实现通过前后端协同，完成了收藏添加、取消、列表查看、批量操作等核心能力，同时兼顾了幂等性、数据一致性和用户体验。

逻辑删除的设计保留了用户行为数据，便于后续的数据分析和功能迭代；批量操作和状态校验进一步提升了功能的实用性和易用性。该功能的落地能够有效提升用户对平台的粘性，为用户购物决策提供便利。