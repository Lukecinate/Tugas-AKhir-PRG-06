-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 22, 2023 at 01:04 PM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 8.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `suggestionsystem`
--

-- --------------------------------------------------------

--
-- Table structure for table `feedback`
--

CREATE TABLE `feedback` (
  `id` int(11) NOT NULL,
  `area_id` int(11) NOT NULL,
  `created_date` datetime NOT NULL,
  `deadline` datetime NOT NULL,
  `post_photo` text DEFAULT NULL,
  `pre_photo` text NOT NULL,
  `pre_status` varchar(50) NOT NULL,
  `post_status` varchar(50) DEFAULT NULL,
  `suggest_name` varchar(100) NOT NULL,
  `suggestion` text NOT NULL,
  `title` varchar(100) NOT NULL,
  `worker_name` varchar(200) DEFAULT NULL,
  `modified_date` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `feedback`
--

INSERT INTO `feedback` (`id`, `area_id`, `created_date`, `deadline`, `post_photo`, `pre_photo`, `pre_status`, `post_status`, `suggest_name`, `suggestion`, `title`, `worker_name`, `modified_date`) VALUES
(4, 1, '2023-07-05 13:42:12', '2023-07-06 18:42:13', 'post_photo', 'pre_photo', 'Before', 'After', 'Saya', 'Merapihkan barang2 yang berantakan', 'Merapihkan alat yang telah dipakai', 'Si A', '2023-07-11 01:00:04'),
(5, 2, '2023-07-07 13:42:12', '2023-07-10 18:42:13', 'post_photo1', 'pre_photo1', 'Before', 'After', 'Saya', 'Membersihkan mesin yang telah dipakai', 'Membersihkan perkakas', 'Si A', NULL),
(6, 2, '2023-07-07 13:42:12', '2023-07-10 18:42:13', 'post_photo2', 'pre_photo2', 'Before', 'After', 'Saya', 'Kangen sama yang beda pulau', 'Memperbaikin Alat - alat', 'Si Axl', NULL),
(7, 2, '2023-07-07 13:42:12', '2023-07-10 18:42:13', 'post_photo3', 'pre_photo3', 'Before', 'After', 'Saya', 'Terdapat mesin yang rusak dibagian joint', 'Mesin rusak', 'Si Ktn', NULL),
(8, 2, '2023-07-07 13:42:12', '2023-07-10 18:42:13', 'post_photo5', 'pre_photo4', 'Before', 'Ongoing', 'Saya', 'Meisn belum dikonfigurasi ulang', 'Miss calculated pada mesin A', 'Si Rzn', NULL),
(9, 3, '2023-07-07 13:42:12', '2023-07-10 18:42:13', NULL, 'uploads\\SSys_2023-07-11_1689065989438_Desain tanpa judul.png', 'Before', NULL, 'Saya', 'Ruangan bekas pemakaian masih berantakan dan belum dirapihkan', 'Ruang LAB CNC berantakan', NULL, NULL),
(10, 2, '2023-07-07 13:42:12', '2023-07-10 18:42:13', NULL, 'uploads\\SSys_2023-07-11_1689065989438_Desain tanpa judul.png', 'Before', NULL, 'Saya1', 'Ruangan bekas pemakaian masih berantakan dan belum dirapihkan', 'Ruang LAB CNC berantakan', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `pic_area`
--

CREATE TABLE `pic_area` (
  `id` int(11) NOT NULL,
  `area` varchar(100) NOT NULL,
  `pic_name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `pic_area`
--

INSERT INTO `pic_area` (`id`, `area`, `pic_name`) VALUES
(1, 'Lab CNC', 'Pa de'),
(2, 'Ruang UPT Pemesinan', 'Ibnu'),
(3, 'Coba', 'Dulu Ngab'),
(12, 'Hamdeh', 'Dahlah'),
(13, 'qwe', 'Katon'),
(17, 'Coba', 'Lagi'),
(18, 'Apakah bisa', 'Semoga saja'),
(22, '123', '331'),
(24, 'sds', 'ffw');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `feedback`
--
ALTER TABLE `feedback`
  ADD PRIMARY KEY (`id`),
  ADD KEY `area_id` (`area_id`);

--
-- Indexes for table `pic_area`
--
ALTER TABLE `pic_area`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `feedback`
--
ALTER TABLE `feedback`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `pic_area`
--
ALTER TABLE `pic_area`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `feedback`
--
ALTER TABLE `feedback`
  ADD CONSTRAINT `feedback_ibfk_1` FOREIGN KEY (`area_id`) REFERENCES `pic_area` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
