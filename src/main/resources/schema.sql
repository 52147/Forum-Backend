/*
 Navicat Premium Data Transfer

 Source Server         : forum
 Source Server Type    : MySQL
 Source Server Version : 50741 (5.7.41)
 Source Host           : localhost:3306
 Source Schema         : forum

 Target Server Type    : MySQL
 Target Server Version : 50741 (5.7.41)
 File Encoding         : 65001

 Date: 02/04/2023 18:18:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for posts
-- ----------------------------
DROP TABLE IF EXISTS `posts`;
CREATE TABLE `posts` (
                         `id` int(11) NOT NULL AUTO_INCREMENT,
                         `title` varchar(255) NOT NULL,
                         `content` text NOT NULL,
                         `user_id` int(11) NOT NULL,
                         `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (`id`),
                         KEY `user_id` (`user_id`),
                         CONSTRAINT `posts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
                         `id` int(11) NOT NULL AUTO_INCREMENT,
                         `username` varchar(255) NOT NULL,
                         `email` varchar(255) NOT NULL,
                         `password` varchar(255) NOT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1006 DEFAULT CHARSET=utf8;