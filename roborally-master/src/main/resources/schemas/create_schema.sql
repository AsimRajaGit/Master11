-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema roborally
-- -----------------------------------------------------
# DROP SCHEMA IF EXISTS `roborally` ;

-- -----------------------------------------------------
-- Schema roborally
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `roborally` DEFAULT CHARACTER SET utf8 ;

USE `roborally` ;

-- -----------------------------------------------------
-- Table `roborally`.`game`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `game` (
      `ID` INT NOT NULL AUTO_INCREMENT,
      `name` VARCHAR(100) NOT NULL,
      `phase` ENUM("INITIALISATION", "PROGRAMMING", "ACTIVATION", "PLAYER_INTERACTION") NOT NULL,
      `step` INT NOT NULL,
      `boardLayout` BLOB NOT NULL,
      `currentPlayer` INT NULL DEFAULT NULL,
      PRIMARY KEY (`ID`),
      INDEX `fk_game_player_idx` (`currentPlayer` ASC) VISIBLE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `roborally`.`player`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `player` (
    `ID` INT NOT NULL,
    `gameID` INT NOT NULL,
    `playerName` VARCHAR(45) NOT NULL,
    `posX` INT NOT NULL,
    `posY` INT NOT NULL,
    `heading` ENUM("NORTH", "EAST", "SOUTH", "WEST") NOT NULL,
    `colour` VARCHAR(50) NULL DEFAULT NULL,
    `order` INT NOT NULL,
    PRIMARY KEY (`ID`, `gameID`),
    INDEX `fk_player_game1_idx` (`gameID` ASC) VISIBLE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `roborally`.`playerHand`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `playerHand` (
    `playerID` INT NOT NULL,
    `gameID` INT NOT NULL,
    `card0` ENUM("FORWARD", "RIGHT", "LEFT", "FAST_FORWARD", "OPTION_LEFT_RIGHT") NULL,
    `card1` ENUM("FORWARD", "RIGHT", "LEFT", "FAST_FORWARD", "OPTION_LEFT_RIGHT") NULL,
    `card2` ENUM("FORWARD", "RIGHT", "LEFT", "FAST_FORWARD", "OPTION_LEFT_RIGHT") NULL,
    `card3` ENUM("FORWARD", "RIGHT", "LEFT", "FAST_FORWARD", "OPTION_LEFT_RIGHT") NULL,
    `card4` ENUM("FORWARD", "RIGHT", "LEFT", "FAST_FORWARD", "OPTION_LEFT_RIGHT") NULL,
    `card5` ENUM("FORWARD", "RIGHT", "LEFT", "FAST_FORWARD", "OPTION_LEFT_RIGHT") NULL,
    `card6` ENUM("FORWARD", "RIGHT", "LEFT", "FAST_FORWARD", "OPTION_LEFT_RIGHT") NULL,
    `card7` ENUM("FORWARD", "RIGHT", "LEFT", "FAST_FORWARD", "OPTION_LEFT_RIGHT") NULL,
    PRIMARY KEY (`playerID`, `gameID`),
    INDEX `fk_playerHand_game1_idx` (`gameID` ASC) VISIBLE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `roborally`.`playerRegister`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `playerRegister` (
    `playerID` INT NOT NULL,
    `gameID` INT NOT NULL,
    `card0` ENUM("FORWARD", "RIGHT", "LEFT", "FAST_FORWARD", "OPTION_LEFT_RIGHT") NULL,
    `card1` ENUM("FORWARD", "RIGHT", "LEFT", "FAST_FORWARD", "OPTION_LEFT_RIGHT") NULL,
    `card2` ENUM("FORWARD", "RIGHT", "LEFT", "FAST_FORWARD", "OPTION_LEFT_RIGHT") NULL,
    `card3` ENUM("FORWARD", "RIGHT", "LEFT", "FAST_FORWARD", "OPTION_LEFT_RIGHT") NULL,
    `card4` ENUM("FORWARD", "RIGHT", "LEFT", "FAST_FORWARD", "OPTION_LEFT_RIGHT") NULL,
    PRIMARY KEY (`playerID`, `gameID`),
    INDEX `fk_playerHand_game1_idx` (`gameID` ASC) VISIBLE)
    ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- MySQL Workbench Synchronization
-- Generated: 2020-03-31 10:10
-- Model: New Model
-- Version: 1.0
-- Project: Name of the project
-- Author: NetLease PC 3

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

ALTER TABLE `player`
    ADD CONSTRAINT `fk_player_game`
        FOREIGN KEY (`gameID`)
            REFERENCES `game` (`ID`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

ALTER TABLE `game`
    ADD CONSTRAINT `fk_game_player`
        FOREIGN KEY (`currentPlayer`)
            REFERENCES `player` (`ID`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

ALTER TABLE `playerHand`
    ADD CONSTRAINT `fk_playerHand_player`
        FOREIGN KEY (`playerID`)
            REFERENCES `player` (`ID`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    ADD CONSTRAINT `fk_playerHand_game`
        FOREIGN KEY (`gameID`)
            REFERENCES `game` (`ID`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

ALTER TABLE `playerRegister`
    ADD CONSTRAINT `fk_playerRegister_player1`
        FOREIGN KEY (`playerID`)
            REFERENCES `player` (`ID`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    ADD CONSTRAINT `fk_playerRegister_game1`
        FOREIGN KEY (`gameID`)
            REFERENCES `game` (`ID`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

