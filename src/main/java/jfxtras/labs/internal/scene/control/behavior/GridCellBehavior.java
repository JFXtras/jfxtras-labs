/**
 * GridCellBehavior.java
 *
 * Copyright (c) 2011-2015, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.internal.scene.control.behavior;

import java.util.ArrayList;

import javafx.scene.control.Control;
import javafx.scene.control.FocusModel;
import javafx.scene.control.MultipleSelectionModel;
import jfxtras.labs.scene.control.grid.GridCell;

import com.sun.javafx.scene.control.behavior.CellBehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;

/**
 * 
 * @author Hendrik Ebbers
 * 
 */
public class GridCellBehavior<T> extends CellBehaviorBase<GridCell<T>> {
	public GridCellBehavior(GridCell<T> control) {
		super(control,new ArrayList<KeyBinding>());
	}

//	@Override
	protected void edit(GridCell<T> arg0) {
		// TODO Auto-generated method stub
		
	}

//	@Override
	protected Control getCellContainer() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
	protected FocusModel<?> getFocusModel() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
	protected MultipleSelectionModel<?> getSelectionModel() {
		// TODO Auto-generated method stub
		return null;
	}
}
