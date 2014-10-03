/*
  	Copyright (c) 2013 GoPivotal, Inc. All Rights Reserved.

	This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; only version 2 of the License, and no
    later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

	The full text of the GPL is provided in the COPYING file.
*/
package pivotal.au.fe.greenplumweb.reports;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.jsp.jstl.sql.Result;

import pivotal.au.fe.greenplumweb.main.GreenplumException;

public interface Query
{
  public Result invoke (Connection conn) throws GreenplumException, SQLException;
  public void setQueryDescription(String queryDescription);
  public void setQuery (String query);
  
}
