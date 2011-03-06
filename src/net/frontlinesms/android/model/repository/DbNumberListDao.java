/**
 * 
 */
package net.frontlinesms.android.model.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.frontlinesms.android.model.model.NumberListMember;

import android.content.ContentResolver;

import net.frontlinesms.android.db.BaseDbAccessObject;
import net.frontlinesms.android.db.FrontlineSmsSqliteHelper;

/**
 * @author aga
 */
public class DbNumberListDao extends BaseDbAccessObject implements NumberListDao {
	public DbNumberListDao(ContentResolver contentResolver) {
		super(contentResolver);
	}
	
	/** @see net.frontlinesms.android.db.BaseDbAccessObject#getBaseUri() */
	@Override
	protected String getBaseUri() {
		return FrontlineSmsSqliteHelper.CONTENT_URI;
	}

	/** @see NumberListDao#addToList(String, String) */
	@Override
	public void addToList(String listName, String phoneNumber) {
		super.save(NumberListMember.create(listName, phoneNumber));
	}

	/** @see NumberListDao#exists(String) */
	@Override
	public boolean exists(String listName) {
		NumberListMember example = new NumberListMember();
		example.setListName(listName);
		return super.get(example).size() > 0;
	}

	/** @see NumberListDao#getNumbers(String) */
	@Override
	public Set<String> getNumbers(String listName) {
		NumberListMember example = new NumberListMember();
		example.setListName(listName);
		List<NumberListMember> fetchedMembers = super.get(example);
		
		Set<String> listMemberNumbers = new HashSet<String>();
		for(NumberListMember member : fetchedMembers) {
			listMemberNumbers.add(member.getPhoneNumber());
		}
		return listMemberNumbers;
	}

	/** @see NumberListDao#removeFromList(String, String) */
	@Override
	public void removeFromList(String listName, String phoneNumber) {
		super.delete(NumberListMember.create(listName, phoneNumber));
	}
}
