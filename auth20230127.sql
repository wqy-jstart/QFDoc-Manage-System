-- MariaDB dump 10.19  Distrib 10.5.18-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: auth
-- ------------------------------------------------------
-- Server version	10.5.18-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `a_permission`
--

DROP TABLE IF EXISTS `a_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `a_permission` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '数据id',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `value` varchar(255) DEFAULT NULL COMMENT '值',
  `description` varchar(255) DEFAULT NULL COMMENT '简介',
  `sort` tinyint(3) unsigned DEFAULT 0 COMMENT '排序序号',
  `gmt_create` datetime DEFAULT NULL COMMENT '数据创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '数据最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='权限';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `a_permission`
--

LOCK TABLES `a_permission` WRITE;
/*!40000 ALTER TABLE `a_permission` DISABLE KEYS */;
INSERT INTO `a_permission` VALUES (1,'读取用户','/user/read','读取用户信息',255,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(2,'添加用户','/user/insert','添加用户',254,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(3,'删除用户','/user/delete','删除用户',253,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(4,'修改用户','/user/update','修改用户信息，含修改密码、启用、禁用、修改基本资料，及其它修改操作',252,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(5,'创建空间','/space/create','添加文件存储空间',99,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(6,'删除空间','/space/delete','删除文件存储空间',98,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(7,'读取空间','/space/list','查询用户存储空间的列表',97,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(8,'空间权限','/space/updateToAuth','修改存储空间的访问权限(公开/私有)',0,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(9,'设置标签','/space/AddTags','设置存储空间的标签',0,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(10,'删除标签','/space/delTags','删除存储空间的标签',0,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(11,'上传文件','/file/upload','上传文件，含图片，txt文件等',96,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(12,'复制文件','/file/copy','复制文件',0,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(13,'删除文件','/file/delete','删除文件',0,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(14,'修改存储状态','/file/updateSpaceStatus','修改文件的存储状态(启用/禁用)',0,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(15,'修改存储类型','/file/updateSpaceType','修改文件的存储类型',0,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(16,'修改过期时间','/file/updateDelTime','修改文件的过期删除时间',0,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(17,'分配权限','/auth/assign','分配角色和权限',0,'2022-07-08 11:30:44','2022-07-08 11:30:44');
/*!40000 ALTER TABLE `a_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `a_role`
--

DROP TABLE IF EXISTS `a_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `a_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '数据id',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `description` varchar(255) DEFAULT NULL COMMENT '简介',
  `sort` tinyint(3) unsigned DEFAULT 0 COMMENT '排序序号',
  `gmt_create` datetime DEFAULT NULL COMMENT '数据创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '数据最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `a_role`
--

LOCK TABLES `a_role` WRITE;
/*!40000 ALTER TABLE `a_role` DISABLE KEYS */;
INSERT INTO `a_role` VALUES (1,'超级管理员','最高权限的管理员角色，应该关联所有权限',255,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(2,'数据管理员','管理文档数据',255,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(3,'修改管理员','负责修改文档的相关功能',99,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(4,'设置管理员','负责设置文档的相关功能',0,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(5,'删除管理员','负责删除文档的相关功能',0,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(6,'权限管理员','负责分配权限的相关功能',0,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(7,'普通用户','创建存储空间、文件上传、文件资源复制',0,'2022-07-08 11:30:44','2022-07-08 11:30:44');
/*!40000 ALTER TABLE `a_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `a_role_permission`
--

DROP TABLE IF EXISTS `a_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `a_role_permission` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '数据id',
  `role_id` bigint(20) unsigned DEFAULT NULL COMMENT '角色id',
  `permission_id` bigint(20) unsigned DEFAULT NULL COMMENT '权限id',
  `gmt_create` datetime DEFAULT NULL COMMENT '数据创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '数据最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色权限关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `a_role_permission`
--

LOCK TABLES `a_role_permission` WRITE;
/*!40000 ALTER TABLE `a_role_permission` DISABLE KEYS */;
INSERT INTO `a_role_permission` VALUES (1,1,1,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(2,1,2,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(3,1,3,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(4,1,4,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(5,1,5,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(6,1,6,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(7,1,7,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(8,1,8,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(9,1,9,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(10,1,10,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(11,1,11,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(12,1,12,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(13,1,13,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(14,1,14,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(15,1,15,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(16,1,16,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(17,1,17,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(18,2,1,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(19,2,7,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(20,3,4,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(21,3,14,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(22,3,15,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(23,3,16,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(24,4,2,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(25,4,8,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(26,4,9,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(27,5,3,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(28,5,6,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(29,5,10,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(30,5,13,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(31,6,1,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(32,6,17,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(33,7,5,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(34,7,11,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(35,7,12,'2022-07-08 11:30:44','2022-07-08 11:30:44');
/*!40000 ALTER TABLE `a_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `a_user`
--

DROP TABLE IF EXISTS `a_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `a_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '数据id',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `password` char(64) DEFAULT NULL COMMENT '密码（密文）',
  `gender` varchar(25) DEFAULT NULL COMMENT '性别',
  `age` tinyint(32) unsigned DEFAULT NULL COMMENT '年龄',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `phone` varchar(50) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(50) DEFAULT NULL COMMENT '电子邮箱',
  `sign` varchar(255) DEFAULT NULL COMMENT '个性签名',
  `enable` tinyint(32) unsigned DEFAULT NULL COMMENT '是否启用(1.启用 0.禁用)',
  `gmt_create` datetime DEFAULT NULL COMMENT '数据创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '数据最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `a_user`
--

LOCK TABLES `a_user` WRITE;
/*!40000 ALTER TABLE `a_user` DISABLE KEYS */;
INSERT INTO `a_user` VALUES (1,'super_admin','$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCu6v2XyFDtC4q.jeeGm/TEZyj15C','男',20,'超级管理员','https://img2.baidu.com/it/u=4244269751,4000533845&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500','13900139001','root@baidu.com','最高权限的管理员',1,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(2,'data_admin','$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCu6v2XyFDtC4q.jeeGm/TEZyj15C','男',20,'数据管理员','https://img0.baidu.com/it/u=1600969112,4145041554&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500','13900139002','super_admin@baidu.cn','超级管理员，通常具有除了【管理管理员】以外的全部权限',1,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(3,'update_admin','$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCu6v2XyFDtC4q.jeeGm/TEZyj15C','男',20,'修改管理员','https://img1.baidu.com/it/u=873106765,2587410047&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1663606800&t=c8de61604dbff6118ae140268f4e3c67','13900139003','liucangsong@baidu.cn','刘苍松老师的账号',1,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(4,'insert_admin','$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCu6v2XyFDtC4q.jeeGm/TEZyj15C','男',20,'添加管理员','https://img2.baidu.com/it/u=3062813899,1142128231&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1663606800&t=e153f24b4499fce32df24cb1ea1b9efa','13900139004','wangkejing@qq.com','王克晶老师的账号',1,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(5,'delete_admin','$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCu6v2XyFDtC4q.jeeGm/TEZyj15C','男',20,'删除管理员','https://img2.baidu.com/it/u=2704182461,2749837878&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500','13900139005','fanchuanqi@baidu.com','范传奇老师的账号',1,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(6,'auth_user','$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCu6v2XyFDtC4q.jeeGm/TEZyj15C','男',20,'权限管理员','https://img2.baidu.com/it/u=4244269751,4000533845&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500','13900139005','fanchuanqi@baidu.com','范传奇老师的账号',1,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(7,'user','$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCu6v2XyFDtC4q.jeeGm/TEZyj15C','男',20,'普通用户','https://img2.baidu.com/it/u=4244269751,4000533845&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500','13900139005','fanchuanqi@baidu.com','范传奇老师的账号',1,'2022-07-08 11:30:44','2022-07-08 11:30:44');
/*!40000 ALTER TABLE `a_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `a_user_role`
--

DROP TABLE IF EXISTS `a_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `a_user_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '数据id',
  `admin_id` bigint(20) unsigned DEFAULT NULL COMMENT '用户id',
  `role_id` bigint(20) unsigned DEFAULT NULL COMMENT '角色id',
  `gmt_create` datetime DEFAULT NULL COMMENT '数据创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '数据最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `a_user_role`
--

LOCK TABLES `a_user_role` WRITE;
/*!40000 ALTER TABLE `a_user_role` DISABLE KEYS */;
INSERT INTO `a_user_role` VALUES (1,1,1,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(2,2,2,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(3,3,3,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(4,4,4,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(5,5,5,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(6,6,6,'2022-07-08 11:30:44','2022-07-08 11:30:44'),(7,7,7,'2022-07-08 11:30:44','2022-07-08 11:30:44');
/*!40000 ALTER TABLE `a_user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-01-27 18:07:05
