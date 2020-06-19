package com.barclayadunn.google;

/*
Google Ads:
Google displays ads alongside search results.
Advertisers are charged when a user clicks on an ad.
Ad auction (max bid per keyword is chosen by advertisers)
Each ad has a daily budget, and Google cannot charge the advertiser beyond that budget.
Design a distributed system that can serve ads quickly while minimizing budget overrun.

ad (ad + keyword) | max-bid | daily-budget

DC1 (set of advertisers + budgets) DC2 DC3

assume millions of advertisers,

where to store the ads
where to store the bids
considerations:
	specificity to viewer
	region
	legality

 */
public class AdServ {
}
