-- Travel Web backend database schema (MySQL 8+)
-- 库名：travel_web（与 application-dev.yml 对应）
-- 所有景点数据已替换为宁波市真实景点

CREATE DATABASE IF NOT EXISTS travel_web DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE travel_web;

-- 用户表
DROP TABLE IF EXISTS t_order_item;
DROP TABLE IF EXISTS t_order;
DROP TABLE IF EXISTS t_itinerary_item;
DROP TABLE IF EXISTS t_scenic_spot;
DROP TABLE IF EXISTS t_user;
DROP TABLE IF EXISTS t_admin;

CREATE TABLE t_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(64) NOT NULL UNIQUE,
  password_hash VARCHAR(128) NOT NULL,
  status VARCHAR(16) NOT NULL DEFAULT 'NORMAL',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 管理员表
CREATE TABLE t_admin (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(64) NOT NULL UNIQUE,
  password_hash VARCHAR(128) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 景点表
CREATE TABLE t_scenic_spot (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(128) NOT NULL,
  category VARCHAR(64) NOT NULL,
  price INT NOT NULL DEFAULT 0,
  address VARCHAR(255) NULL,
  image VARCHAR(512) NULL,
  description TEXT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_category (category),
  INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 行程表（用户-景点）
CREATE TABLE t_itinerary_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  scenic_id BIGINT NOT NULL,
  note VARCHAR(255) NULL,
  sort_order INT NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uq_user_scenic (user_id, scenic_id),
  INDEX idx_user (user_id),
  CONSTRAINT fk_itinerary_user FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE,
  CONSTRAINT fk_itinerary_scenic FOREIGN KEY (scenic_id) REFERENCES t_scenic_spot(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 订单表
CREATE TABLE t_order (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  total_price INT NOT NULL DEFAULT 0,
  status VARCHAR(32) NOT NULL DEFAULT '待出行',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_user (user_id),
  CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 订单明细表
CREATE TABLE t_order_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  scenic_id BIGINT NOT NULL,
  scenic_name VARCHAR(128) NOT NULL,
  scenic_price INT NOT NULL DEFAULT 0,
  note VARCHAR(255) NULL,
  sort_order INT NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_order (order_id),
  CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES t_order(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 初始化管理员账号：admin / 123456
-- 密码哈希算法：SHA-256( 'travel-web-demo-salt' + rawPassword )
-- ============================================================
INSERT INTO t_admin(username, password_hash)
VALUES ('admin', 'b3d12fc67e01a813573c3cc40cc5d89940185f62b3545ecfd8c7468764c521ef')
ON DUPLICATE KEY UPDATE password_hash = VALUES(password_hash);

-- ============================================================
-- 初始化 8 个宁波市真实景点数据
-- ============================================================
INSERT INTO t_scenic_spot (id, name, category, price, address, image, description)
VALUES
  (1, '天一阁博物院', '历史人文', 30, '宁波市海曙区天一街10号',
   'https://picsum.photos/seed/ningbo-tianyi/800/500',
   '天一阁是中国现存最早、亚洲最古老的私家藏书楼，建于明嘉靖四十年（1561年），由兵部右侍郎范钦创建。天一阁博物院以藏书楼为核心，集藏书文化、园林艺术、古建筑群于一体，被誉为"南国书城"。院内珍藏古籍30余万卷，其中善本8万余卷，是研究中国古代文化史和藏书史的重要基地。园内亭台楼阁错落有致，假山流水相映成趣，是宁波最负盛名的文化地标。'),

  (2, '月湖公园', '自然风光', 0, '宁波市海曙区偃月街',
   'https://picsum.photos/seed/ningbo-yuehu/800/500',
   '月湖开凿于唐代，因形如弯月而得名，是宁波市区最具代表性的城市湖泊公园。公园占地约40公顷，湖面面积约16公顷，环湖遍植柳树和桂花，四季景致各异。湖畔分布着天一阁、居士林、贺秘监祠等多处古迹，湖中设有湖心岛、湖心亭等景点。月湖不仅是宁波市民休闲散步的首选之地，也是体验"书藏古今，港通天下"宁波文化的绝佳去处，春日樱花烂漫，秋日桂香飘逸。'),

  (3, '老外滩', '城市地标', 0, '宁波市江北区中马路159号',
   'https://picsum.photos/seed/ningbo-bund/800/500',
   '宁波老外滩是中国最早的对外开埠区之一，比上海外滩的历史还要早20年。如今的老外滩保留了众多中西合璧的历史建筑，包括天主教堂、旧海关大楼、英国领事馆旧址等。沿江的步行街汇集了酒吧、咖啡馆、特色餐厅和文创店铺，是宁波夜生活的核心区域。夜幕降临后，华灯初上，甬江两岸灯火辉煌，三江口的夜景令人流连忘返，是年轻人聚会和游客打卡的热门地标。'),

  (4, '东钱湖旅游度假区', '自然风光', 60, '宁波市鄞州区东钱湖镇',
   'https://picsum.photos/seed/ningbo-dongqian/800/500',
   '东钱湖是浙江省最大的天然淡水湖，面积约22平方公里，是西湖的4倍，被誉为"西湖风光，太湖气魄"。湖区群山环抱，湖水清澈，拥有小普陀、陶公岛、南宋石刻公园、雅戈尔动物园等多个核心景点。东钱湖盛产湖鲜，湖边的农家乐可以品尝到地道的宁波本帮菜。游客可以乘船游湖、环湖骑行或徒步登山，是宁波近郊休闲度假的绝佳选择，四季皆宜游览。'),

  (5, '鼓楼(海曙楼)', '历史人文', 0, '宁波市海曙区公园路2号',
   'https://picsum.photos/seed/ningbo-gulou/800/500',
   '宁波鼓楼又名海曙楼，始建于唐长庆元年（821年），是宁波现存最古老的城楼建筑，也是宁波城市的标志性古迹。鼓楼为三层木石结构，底层是唐代的城门遗址，顶层有一座巨大的铜钟。登上鼓楼可以俯瞰海曙老城区的风貌，周边是鼓楼步行街，汇集了众多宁波老字号小吃和特色商铺。鼓楼步行街上的油赞子、豆酥糖、宁波汤圆等地方美食深受游客喜爱，是感受老宁波市井生活的最佳去处。'),

  (6, '宁波博物馆', '历史人文', 0, '宁波市鄞州区首南中路1000号',
   'https://picsum.photos/seed/ningbo-museum/800/500',
   '宁波博物馆由普利兹克建筑奖获得者、著名建筑师王澍设计，建筑外观采用"新乡土主义"风格，外墙使用从宁波老城改造中回收的旧砖瓦和竹条模板混凝土建造，呈现出独特的质感和历史记忆。馆藏文物6万余件，常设"宁波历史陈列"、"宁波民俗陈列"和"竹刻艺术陈列"三大主题展，全面展示了宁波七千年的历史文化和海洋文明。博物馆建筑本身就是一件艺术品，被誉为"会呼吸的建筑"。'),

  (7, '慈城古县城', '历史人文', 75, '宁波市江北区慈城镇',
   'https://picsum.photos/seed/ningbo-cicheng/800/500',
   '慈城古县城是中国江南地区保存最完整的古县城之一，始建于唐开元二十六年（738年），至今已有千年历史。古城内保存了完好的明清古建筑群，包括县衙、孔庙、校士馆、冯岳彩绘台门等十多处全国重点文物保护单位。街道格局保存了唐宋时期的"一河一街"、"一河两街"的江南水乡特色。漫步古城，青砖黛瓦、小桥流水，仿佛穿越回千年之前。慈城年糕是当地著名特产，游客可在此体验手工年糕的制作。'),

  (8, '象山影视城', '主题乐园', 150, '宁波市象山县新桥镇',
   'https://picsum.photos/seed/ningbo-xiangshan/800/500',
   '象山影视城是中国十大影视基地之一，也是《神雕侠侣》《琅琊榜》《芈月传》等众多热门影视剧的取景地。影视城占地面积约1175亩，建有神雕侠侣城、春秋战国城、民国城、西游记乐园等多个主题景区，还原了不同历史时期的建筑风貌。游客可以观看实景演出、参与影视体验项目、偶遇剧组拍摄。节假日期间还有大型灯光秀和烟火表演。影视城所在的象山半岛也以海鲜美食闻名，适合全家一日游。')
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  category = VALUES(category),
  price = VALUES(price),
  address = VALUES(address),
  image = VALUES(image),
  description = VALUES(description);
