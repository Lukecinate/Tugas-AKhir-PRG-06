-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 08, 2023 at 11:23 AM
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
  `area_id` int(11) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `deadline` datetime DEFAULT NULL,
  `post_photo` text DEFAULT NULL,
  `pre_photo` text DEFAULT NULL,
  `pre_status` varchar(50) DEFAULT NULL,
  `post_status` varchar(50) DEFAULT NULL,
  `suggest_name` varchar(100) DEFAULT NULL,
  `suggestion` text DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `worker_name` varchar(200) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `feedback`
--

INSERT INTO `feedback` (`id`, `area_id`, `created_date`, `deadline`, `post_photo`, `pre_photo`, `pre_status`, `post_status`, `suggest_name`, `suggestion`, `title`, `worker_name`, `modified_date`) VALUES
(62, 2, '2023-08-02 01:23:48', '2023-08-05 08:00:00', NULL, 'Syss-Pre-fc80ab72-89a0-4993-a7d9-ed467f19b491.jpg', 'Before', NULL, 'Si Dia', 'Tolong dibersihkan dan bereskan akan terlihat rapih', 'Meja UPT Pemesinan', NULL, NULL),
(63, 17, '2023-08-02 01:24:40', '2023-08-04 09:30:00', 'Syss-Post-1b175aa6-4d77-4c06-9945-e27e1f685608.jpg', 'Syss-Pre-71a3372f-e9fd-4083-bc2e-965f44dd8805.jpg', 'Before', 'After', 'Si Dia', 'Toling disapu dan dipel agar terlihat bersih dan nyaman', 'Lantai Lab Benchwork', 'Eko Abdul Goffar', '2023-08-07 16:03:50'),
(65, 3, '2023-08-02 01:28:43', '2023-08-04 07:30:00', NULL, 'Syss-Pre-6935f97c-8169-43ef-8847-e0180567cce8.jpg', 'Before', NULL, 'Si Dia', 'Meja dan lemari alat tolong dibersihkan dan bereskan', 'Meja & Lemari Kamar Alat', NULL, NULL),
(68, 13, '2023-08-02 02:24:01', '2023-08-05 10:00:00', NULL, 'Syss-Pre-9b493503-3dbc-4c13-90d1-4f8da8d09bcd.jpg', 'Before', NULL, 'ABDUL AZIZ', 'Tolong dibersihkan agar terlihat bagus', 'Mesin Kotor', NULL, NULL),
(69, 3, '2023-08-02 02:24:51', '2023-08-04 07:30:00', 'Syss-Post-c7ccb763-fbc2-4bcb-b0cf-4787e5a669fe.jpg', 'Syss-Pre-a40e4c9e-817f-4cd0-9657-558147f0e9e5.jpg', 'Before', 'After', 'ABDUL AZIZ', 'Bereskan dan bersihkan alat yang telah dipakai', 'Alat Berserakan', 'ABET.H.MANULLANG', '2023-08-08 11:40:13'),
(70, 2, '2023-08-02 02:26:28', '2023-08-04 07:30:00', NULL, 'Syss-Pre-d6d63098-b717-45d8-a124-8ec7a160cb08.jpg', 'Before', 'Ongoing', 'ABDUL AZIZ', 'Meja tolong dibereskan agar menjadi contoh bagi Mahasiswa', 'Meja Berantakan', NULL, NULL),
(71, 2, '2023-08-02 02:27:45', '2023-08-04 15:30:00', 'Syss-Post-fa585551-e95a-418a-81f7-a2ec95fb29a8.jpg', 'Syss-Pre-354c2cf2-c4b0-4b5a-ac66-ea62d90a21ab.jpg', 'Before', 'After', 'ABDUL AZIZ', 'Mohon dibersihkan ketika minuman telah habis', 'Bekas Minum Kopi', 'ARKA BASWARA BIMO SAKTI', '2023-08-03 10:40:13'),
(72, 13, '2023-08-02 02:28:41', '2023-08-05 13:00:00', NULL, 'Syss-Pre-b84b523d-d608-4401-bcbc-aa37f9509927.jpg', 'Before', NULL, 'ABDUL AZIZ', 'Tolong pindahkan ketika sudah selesai dalam pemakaian', 'Alat berat ditengah jalan', NULL, NULL),
(73, 17, '2023-08-02 02:31:23', '2023-08-05 09:30:00', NULL, 'Syss-Pre-50b66c68-98dd-4f16-ba83-9de16c6b4e06.jpg', 'Before', NULL, 'ABDUL AZIZ', 'Tolong ketika telah selesai memakai untuk dibereskan, tidak pantas dilihat', 'Mesin Berantakan', NULL, NULL),
(74, 1, '2023-08-02 02:34:32', '2023-08-06 15:30:00', NULL, 'Syss-Pre-0398e843-c2fd-44d6-a586-b8a24feffb54.jpg', 'Before', NULL, 'ABDUL AZIZ', 'Tolong dibersihkan segera', 'Oli Berceceran di Tembok', NULL, NULL),
(75, 2, '2023-08-02 02:53:42', '2023-08-05 08:30:00', 'Syss-Post-c7ea1258-4ca4-4b74-9d6f-bbbea6051365.jpg', 'Syss-Pre-77a6757f-d579-432f-aefe-261e9d9ccb27.jpg', 'Before', 'After', 'ARKA BASWARA BIMO SAKTI', 'Bersihkan dan bereskan, tidak enak dilihat oleh Mahasiswa', 'Ruangan UPT Pemesinan', 'ARKA BASWARA BIMO SAKTI', '2023-08-01 15:40:13'),
(76, 3, '2023-08-02 07:29:43', '2023-08-25 17:30:00', NULL, 'Syss-Pre-d5a90151-c2d6-4b5e-9b7d-0f85432c216f.jpg', 'Before', NULL, 'IKA MAYLIA', 'hapus data tidak terpakai', 'Bersihkan data', NULL, NULL),
(77, 1, '2023-08-02 08:28:22', '2023-08-10 06:30:00', NULL, 'Syss-Pre-dcb9b473-1bcb-449b-99d7-233c05e1a7f9.jpg', 'Before', 'Ongoing', 'ABDUL AZIZ', 'bersihkan lantai', 'Lantai Lab CNC', NULL, NULL),
(78, 13, '2023-08-08 09:21:15', '2023-08-31 09:20:46', 'Syss-Post-f8acf583-796c-4a56-ae99-3703388cc3cd.jpg', 'Syss-Pre-a37666e1-3450-441b-9e60-53229dec2dc7.jpg', 'Before', 'After', 'KATON HAYU NUGROHO', '', 'Buang Besi ', 'KATON HAYU NUGROHO', '2023-08-08 08:40:13'),
(79, 17, '2023-08-08 09:29:44', '2023-08-30 09:29:24', 'Syss-Post-f1a514fd-3289-4afa-9cab-ab46e9b907fd.jpg', 'Syss-Pre-749f17d3-477f-4dd1-b811-9cb37c15e699.jpg', 'Before', 'After', 'KATON HAYU NUGROHO', 'sbshsh', 'cobamodified2', 'KATON HAYU NUGROHO', '2023-08-08 09:30:14'),
(80, 17, '2023-08-08 09:47:53', '2023-08-31 09:47:28', 'Syss-Post-339a6adf-0cc8-4a09-bb9a-fceba8214feb.jpg', 'Syss-Pre-6ca1a5ac-7c9b-41ed-95ba-6f9c39a1245d.jpg', 'Before', 'After', 'KATON HAYU NUGROHO', 'Pel Lantai', 'Pel Lantai', 'KATON HAYU NUGROHO', '2023-08-08 09:48:25'),
(81, 17, '2023-08-08 15:22:58', '2023-08-10 07:30:00', NULL, 'Syss-Pre-f003ddca-47b4-4c80-ae58-3be6de2d4efc.png', 'Before', NULL, 'ABDUL AZIZ', 'Tolong dibereskan dan bersihkan ', 'Meja Lab Berantakan ', NULL, '2023-08-08 15:23:00'),
(82, 13, '2023-08-08 15:28:46', '2023-08-10 08:00:00', NULL, 'Syss-Pre-a39b0d08-a23e-4f05-8e9b-1b43768f4fc1.png', 'Before', NULL, 'ABDUL AZIZ', 'Tolong bersihkan dan bereskan ', 'Meja Lab Berantakan dan Tidak Rapih ', NULL, '2023-08-08 15:28:47'),
(83, 3, '2023-08-08 15:35:01', '2023-08-10 08:00:00', 'Syss-Post-a197d236-3344-4396-9467-fe560e1d33f3.png', 'Syss-Pre-afcfd14e-0b71-4ed8-8232-da59cd9c4cae.png', 'Before', 'After', 'ABDUL AZIZ', 'Tolong dibersihkan dan bereskan ', 'Alat Berantakan ', 'ABDUL AZIZ', '2023-08-08 15:40:13');

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
(1, 'Lab CNC', 'Dwima'),
(2, 'Ruang UPT Pemesinan', 'Sunarjo'),
(3, 'Ruang Kamar Alat', 'Pakde Kartum'),
(13, 'Lab CMM', 'Herry S.'),
(17, 'Lab Benchwork', 'Katon');

-- --------------------------------------------------------

--
-- Stand-in structure for view `report`
-- (See below for the actual view)
--
CREATE TABLE `report` (
`title` varchar(100)
,`pre_photo` text
,`post_photo` text
,`suggestion` text
,`worker_name` varchar(200)
,`area` varchar(100)
,`modified_date` datetime
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `report1`
-- (See below for the actual view)
--
CREATE TABLE `report1` (
`title` varchar(100)
,`pre_photo` text
,`post_photo` text
,`suggestion` text
,`worker_name` varchar(200)
,`area` varchar(100)
,`modified_date` datetime
);

-- --------------------------------------------------------

--
-- Structure for view `report`
--
DROP TABLE IF EXISTS `report`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `report`  AS SELECT `f`.`title` AS `title`, `f`.`pre_photo` AS `pre_photo`, `f`.`post_photo` AS `post_photo`, `f`.`suggestion` AS `suggestion`, `f`.`worker_name` AS `worker_name`, `p`.`area` AS `area`, `f`.`modified_date` AS `modified_date` FROM (`feedback` `f` join `pic_area` `p` on(`f`.`area_id` = `p`.`id`)) WHERE `f`.`post_status` = 'After''After'  ;

-- --------------------------------------------------------

--
-- Structure for view `report1`
--
DROP TABLE IF EXISTS `report1`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `report1`  AS SELECT `f`.`title` AS `title`, `f`.`pre_photo` AS `pre_photo`, `f`.`post_photo` AS `post_photo`, `f`.`suggestion` AS `suggestion`, `f`.`worker_name` AS `worker_name`, `p`.`area` AS `area`, `f`.`modified_date` AS `modified_date` FROM (`feedback` `f` join `pic_area` `p` on(`p`.`id` = `f`.`area_id`)) WHERE `f`.`post_status` = 'Ongoing''Ongoing'  ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `feedback`
--
ALTER TABLE `feedback`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKg6nk6se3uceixhykh2vwsnuib` (`area_id`);

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=84;

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
  ADD CONSTRAINT `FKg6nk6se3uceixhykh2vwsnuib` FOREIGN KEY (`area_id`) REFERENCES `pic_area` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
