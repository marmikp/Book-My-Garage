-- phpMyAdmin SQL Dump
-- version 4.5.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jan 24, 2019 at 04:50 PM
-- Server version: 10.1.13-MariaDB
-- PHP Version: 5.6.20

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `charlie_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `garage_location`
--

CREATE TABLE `garage_location` (
  `id` int(5) NOT NULL,
  `username` varchar(50) NOT NULL,
  `location` varchar(100) DEFAULT NULL,
  `status` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `garage_location`
--

INSERT INTO `garage_location` (`id`, `username`, `location`, `status`) VALUES
(1, 'jinali@gmail.com', '23.755535893465865,72.54202388226987', 1);

-- --------------------------------------------------------

--
-- Table structure for table `garage_temp_location`
--

CREATE TABLE `garage_temp_location` (
  `id` int(10) NOT NULL,
  `username` varchar(50) NOT NULL,
  `location` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `garage_temp_location`
--

INSERT INTO `garage_temp_location` (`id`, `username`, `location`) VALUES
(1, 'jinali@gmail.com', '23.7623704,72.5609652');

-- --------------------------------------------------------

--
-- Table structure for table `login`
--

CREATE TABLE `login` (
  `id` int(5) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `status` int(2) NOT NULL,
  `name` varchar(50) NOT NULL,
  `mobile` varchar(12) NOT NULL,
  `img_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `login`
--

INSERT INTO `login` (`id`, `username`, `password`, `status`, `name`, `mobile`, `img_name`) VALUES
(1, 'chirag@gmail.com', 'chirag', 0, 'Chirag Joshi', '8141642183', 'IMG-20190122-WA0014.jpg'),
(2, 'jinali@gmail.com', 'jinali', 1, 'Jinali Raval', '8849739018', 'IMG-20190122-WA0011.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `request`
--

CREATE TABLE `request` (
  `id` int(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `location` varchar(100) NOT NULL,
  `message` varchar(500) NOT NULL,
  `work_status` int(2) NOT NULL,
  `flag` int(2) NOT NULL,
  `work_by` varchar(50) NOT NULL,
  `distance` varchar(5) NOT NULL,
  `ask_complete` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `request`
--

INSERT INTO `request` (`id`, `username`, `location`, `message`, `work_status`, `flag`, `work_by`, `distance`, `ask_complete`) VALUES
(1, 'chirag@gmail.com', '23.7552885,72.5416631', 'My car stopped', 0, 0, 'jinali@gmail.com', '0.045', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `garage_location`
--
ALTER TABLE `garage_location`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `garage_temp_location`
--
ALTER TABLE `garage_temp_location`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `login`
--
ALTER TABLE `login`
  ADD PRIMARY KEY (`id`,`username`);

--
-- Indexes for table `request`
--
ALTER TABLE `request`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `garage_location`
--
ALTER TABLE `garage_location`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `garage_temp_location`
--
ALTER TABLE `garage_temp_location`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `login`
--
ALTER TABLE `login`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `request`
--
ALTER TABLE `request`
  MODIFY `id` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
