package com.dixin.hibernate;

public class StatView extends AbstractStatView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5578070094530303447L;

	/** default constructor */
	public StatView() {
	}

	/** full constructor */
	public StatView(Repertory repertory, Integer csCount, Integer faCount) {
		super(repertory, csCount, faCount);
	}
}
