# Ticket Box（票务盒子）

单机微服务版本：前端 → web-bff（HTTP + Sa-Token）→ 各业务服务（Dubbo + Nacos）。

## 启动顺序

1. `docker compose up -d`（MySQL / Redis / Nacos / MinIO）
2. 首次或全新环境执行 `scripts/init-schemas.sql` 创建五个业务库
3. 按顺序启动后端：

| 服务 | HTTP | Dubbo |
|---|---|---|
| identity-service | 8801 | 20880 |
| content-service | 8802 | 20881 |
| inventory-service | 8803 | 20882 |
| payment-service | 8805 | 20884 |
| order-service | 8804 | 20883 |
| web-bff | 8800 | — |

```bash
java -jar services/identity/identity-service/target/identity-service-0.0.1-SNAPSHOT.jar &
java -jar services/content/content-service/target/content-service-0.0.1-SNAPSHOT.jar &
java -jar services/inventory/inventory-service/target/inventory-service-0.0.1-SNAPSHOT.jar &
java -jar services/payment/payment-service/target/payment-service-0.0.1-SNAPSHOT.jar &
java -jar services/order/order-service/target/order-service-0.0.1-SNAPSHOT.jar &
java -jar services/web-bff/target/web-bff-0.0.1-SNAPSHOT.jar &
```

4. 启动前端：

```bash
cd frontend/web && npm run dev
cd frontend/admin && npm run dev
```

## 数据库

每个服务只访问自己的 schema：`ticket_box_identity`、`ticket_box_content`、`ticket_box_inventory`、`ticket_box_order`、`ticket_box_payment`。原 `ticket_box` 库保留为旧单体的回滚数据，不再被新服务读写。

## 构建与测试

```bash
./mvnw clean install
```

BFF 集成测试需要所有业务服务与 Nacos 已启动；各服务自身的测试使用 `test` profile，不占用固定 Dubbo 端口。
