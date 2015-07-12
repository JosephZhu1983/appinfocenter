package me.josephzhu.appinfocenter.site;

public class ApplicationProperties
{
	private String websiteBaseUrl;

	public String getWebsiteStaticFileBaseUrl()
	{
		return websiteStaticFileBaseUrl;
	}

	public void setWebsiteStaticFileBaseUrl(String websiteStaticFileBaseUrl)
	{
		this.websiteStaticFileBaseUrl = websiteStaticFileBaseUrl;
	}

	public String getWebsiteBaseUrl()
	{
		return websiteBaseUrl;
	}

	public void setWebsiteBaseUrl(String websiteBaseUrl)
	{
		this.websiteBaseUrl = websiteBaseUrl;
	}

	private String websiteStaticFileBaseUrl;
}
