package com.mollom.client.rest;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author johan
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BlacklistResponse extends RestResponse {

  @XmlElementWrapper(name="list")
  @XmlElement(name="entry")
  private List<? extends BlacklistEntry> entries;
  private int listOffset;
  private int listCount;
  private int listTotal;

  /**
   * @return the entries
   */
  public List<? extends BlacklistEntry> getEntries() {
    return entries;
  }

  /**
   * @param entries the entries to set
   */
  public void setEntries(List<? extends BlacklistEntry> entries) {
    this.entries = entries;
  }

  /**
   * @return the listOffset
   */
  public int getListOffset() {
    return listOffset;
  }

  /**
   * @param listOffset the listOffset to set
   */
  public void setListOffset(int listOffset) {
    this.listOffset = listOffset;
  }

  /**
   * @return the listCount
   */
  public int getListCount() {
    return listCount;
  }

  /**
   * @param listCount the listCount to set
   */
  public void setListCount(int listCount) {
    this.listCount = listCount;
  }

  /**
   * @return the listTotal
   */
  public int getListTotal() {
    return listTotal;
  }

  /**
   * @param listTotal the listTotal to set
   */
  public void setListTotal(int listTotal) {
    this.listTotal = listTotal;
  }

}
