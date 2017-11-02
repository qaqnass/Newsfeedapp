package com.nanoddegree.newsfeedapp.models;


public class News {
  private String sectionName;
  private String publicationDate;
  private String title;

  public News(String sectionName, String publicationDate, String title) {
    setSectionName(sectionName);
    setPublicationDate(publicationDate);
    setTitle(title);
  }


  public String getSectionName() {
    return sectionName;
  }

  public void setSectionName(String sectionName) {
    this.sectionName = sectionName;
  }

  public String getPublicationDate() {
    return publicationDate;
  }

  public void setPublicationDate(String publicationDate) {
    this.publicationDate = publicationDate;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
