/**
 * MagnifierBuilder.java
 *
 * Copyright (c) 2011-2013, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
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

package jfxtras.labs.scene.control;

import javafx.scene.Node;
import javafx.scene.control.ControlBuilder;
import javafx.util.Builder;

/**
 * Builder class for the {@link Magnifier} control.
 * 
 * @author SaiPradeepDandem
 */
public class MagnifierBuilder<B extends MagnifierBuilder<B>> extends ControlBuilder<B> implements Builder<Magnifier> {
	private double radius;
	private double frameWidth;
	private double scaleFactor;
	private double scopeLineWidth;
	private boolean scopeLinesVisible;
	private Node content;
	private boolean active;
	private boolean scalableOnScroll;
	private boolean resizableOnScroll;

	/**
	 * Creates a new instance of {@link MagnifierBuilder}.
	 * 
	 * @return a new instance of {@link MagnifierBuilder}
	 */
	@SuppressWarnings("rawtypes")
	public static MagnifierBuilder<?> create() {
		return new MagnifierBuilder();
	}

	/**
	 * Sets the value of radius of magnifier viewer.
	 * 
	 * @param paramDouble
	 *            - Radius of the magnifier viewer.
	 * @return {@link MagnifierBuilder}
	 */
	public MagnifierBuilder<B> radius(double paramDouble) {
		this.radius = paramDouble;
		return this;
	}

	/**
	 * Sets the value of frameWidth of magnifier viewer.
	 * 
	 * @param paramDouble
	 *            - FrameWidth of the magnifier viewer.
	 * @return {@link MagnifierBuilder}
	 */
	public MagnifierBuilder<B> frameWidth(double paramDouble) {
		this.frameWidth = paramDouble;
		return this;
	}

	/**
	 * Sets the value of scaleFactor of magnifier.
	 * 
	 * @param paramDouble
	 *            - ScaleFactor of the magnifier.
	 * @return {@link MagnifierBuilder}
	 */
	public MagnifierBuilder<B> scaleFactor(double paramDouble) {
		this.scaleFactor = paramDouble;
		return this;
	}

	/**
	 * Sets the value of scopeLineWidth of magnifier viewer.
	 * 
	 * @param paramDouble
	 *            - ScopeLineWidth of the magnifier viewer.
	 * @return {@link MagnifierBuilder}
	 */
	public MagnifierBuilder<B> scopeLineWidth(double paramDouble) {
		this.scopeLineWidth = paramDouble;
		return this;
	}

	/**
	 * Sets the value of scopeLinesVisible of magnifier viewer.
	 * 
	 * @param paramBoolean
	 *            - ScopeLinesVisible of the magnifier viewer.
	 * @return {@link MagnifierBuilder}
	 */
	public MagnifierBuilder<B> scopeLinesVisible(boolean paramBoolean) {
		this.scopeLinesVisible = paramBoolean;
		return this;
	}

	/**
	 * Sets the value of active of magnifier viewer.
	 * 
	 * @param paramBoolean
	 *            - Active of the magnifier viewer.
	 * @return {@link MagnifierBuilder}
	 */
	public MagnifierBuilder<B> active(boolean paramBoolean) {
		this.active = paramBoolean;
		return this;
	}

	/**
	 * Sets the value of scalableOnScroll of magnifier viewer.
	 * 
	 * @param paramBoolean
	 *            - ScalableOnScroll of the magnifier viewer.
	 * @return {@link MagnifierBuilder}
	 */
	public MagnifierBuilder<B> scalableOnScroll(boolean paramBoolean) {
		this.scalableOnScroll = paramBoolean;
		return this;
	}

	/**
	 * Sets the value of resizableOnScroll of magnifier viewer.
	 * 
	 * @param paramBoolean
	 *            - ResizableOnScroll of the magnifier viewer.
	 * @return {@link MagnifierBuilder}
	 */
	public MagnifierBuilder<B> resizableOnScroll(boolean paramBoolean) {
		this.resizableOnScroll = paramBoolean;
		return this;
	}

	/**
	 * Sets the value of content of magnifier viewer.
	 * 
	 * @param paramNode
	 *            - Content of the magnifier viewer.
	 * @return {@link MagnifierBuilder}
	 */
	public MagnifierBuilder<B> content(Node paramNode) {
		this.content = paramNode;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Magnifier build() {
		final Magnifier magnifier = new Magnifier();
		applyTo(magnifier);
		magnifier.setRadius(this.radius);
		magnifier.setFrameWidth(this.frameWidth);
		magnifier.setScaleFactor(this.scaleFactor);
		magnifier.setScopeLineWidth(this.scopeLineWidth);
		magnifier.setActive(this.active);
		magnifier.setScopeLinesVisible(this.scopeLinesVisible);
		magnifier.setScalableOnScroll(this.scalableOnScroll);
		magnifier.setResizableOnScroll(this.resizableOnScroll);
		magnifier.setContent(this.content);
		return magnifier;
	}
}
