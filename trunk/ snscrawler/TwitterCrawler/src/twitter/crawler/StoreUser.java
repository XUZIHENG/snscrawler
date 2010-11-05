package twitter.crawler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import twitter.crawler.util.DBUtil;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class StoreUser {
	private int page=1;
	private int rp=5000;
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRp() {
		return rp;
	}
	public void setRp(int rp) {
		this.rp = rp;
	}
	public void storeByIDs() throws SQLException, TwitterException{
		OAuthTwitter oAuthTwitter=new OAuthTwitter();
		Twitter twitter=oAuthTwitter.oAuthLogin();
		Connection conn=DBUtil.getConn();
		Statement stmt=DBUtil.createStmt(conn);
		String sql="select id from kaifulee";
		ResultSet rs=DBUtil.query(sql, stmt);
		rs.last();
		long total=rs.getRow();
		do{
			sql="select followerId from kaifulee limit "+(page-1)*rp+","+rp;
			rs=DBUtil.query(sql, stmt);
			int inPageCount=0;
			while(rs.next()){
				User user=twitter.showUser(rs.getInt("followerId"));
				StringBuffer sb=new StringBuffer();
				sb.append("insert into user(" +
						"user_id," +
						"user_name," +
						"user_screenName," +
						"user_description," +
						"user_url," +
						"user_lang," +
						"user_location," +
						"user_statusesCount," +
						"user_followersCount," +
						"user_friendsCount," +
						"user_favouritesCount," +
						"user_listedCount," +
						"user_createdAt," +
						"user_profileImageUrl," +
						"user_profileTextColor," +
						"user_profileLinkColor," +
						"user_profileBackgroundColor," +
						"user_profileBackgroundImageUrl," +
						"user_profileBackgroundTitled," +
						"user_profileSidebarFillColor," +
						"user_profileSidebarBorderColor," +
						"user_utcOffset," +
						"user_timeZone," +
						"user_isGeoEnabled," +
						"user_isProtected," +
						"user_isVerified," +
						"user_isFollowRequestSent," +
						"user_isContributorsEnabled) ");
				sb.append("values(");
				sb.append(user.getId()+",");
				sb.append("'"+user.getName()+"',");
				sb.append("'"+user.getScreenName()+"',");
				sb.append("'"+user.getDescription()+"',");
				sb.append("'"+user.getURL()+"',");
				sb.append("'"+user.getLang()+"',");
				sb.append("'"+user.getLocation()+"'',");
				sb.append(user.getStatusesCount()+",");
				sb.append(user.getFollowersCount()+",");
				sb.append(user.getFriendsCount()+",");
				sb.append(user.getFavouritesCount()+",");
				sb.append(user.getListedCount()+",");
				sb.append("'"+new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(user.getCreatedAt())+"',");
				sb.append("'"+user.getProfileImageURL()+"',");
				sb.append("'"+user.getProfileTextColor()+"',");
				sb.append("'"+user.getProfileLinkColor()+"',");
				sb.append("'"+user.getProfileBackgroundColor()+"',");
				sb.append("'"+user.getProfileBackgroundImageUrl()+"',");
				sb.append("'"+user.isProfileBackgroundTiled()+"',");
				sb.append("'"+user.getProfileSidebarFillColor()+"',");
				sb.append("'"+user.getProfileSidebarBorderColor()+"',");
				sb.append(user.getUtcOffset()+",");
				sb.append("'"+user.getTimeZone()+"',");
				sb.append("'"+user.isGeoEnabled()+"',");
				sb.append("'"+user.isProtected()+"',");
				sb.append("'"+user.isVerified()+"',");
				sb.append("'"+user.isFollowRequestSent()+"',");
				sb.append("'"+user.isContributorsEnabled()+"'");
				sb.append(")");
				System.out.println(sb.toString());
				DBUtil.update(sb.toString(), stmt);
				System.out.println("update twitter database successs:"+(page*rp+inPageCount));
				inPageCount++;
			}
			page++;
		}while ((page-1)*rp>total);
		stmt.close();
		conn.close();
	
	}
	public static void main(String[] args) throws SQLException, TwitterException {
		StoreUser storeUser=new StoreUser();
		storeUser.storeByIDs();
	}
}
