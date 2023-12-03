/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package pisd.dk.dtu.compute.se.roborally.model;

import org.jetbrains.annotations.NotNull;

import pisd.dk.dtu.compute.se.designpatterns.observer.Subject;

/**
 * This class converts the commands from the enum Command to an CommandCard-object and gives it a name as a string.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class CommandCard extends Subject {

    final public Command command;

    public CommandCard(@NotNull Command command) {
        this.command = command;
    }

    public String getName() {
        return command.displayName;
    }


}
