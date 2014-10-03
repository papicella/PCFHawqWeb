package pivotal.au.fe.greenplumweb.dao.sessions;

import pivotal.au.fe.greenplumweb.main.GreenplumException;
import pivotal.au.fe.greenplumweb.main.Result;

import java.util.List;

public interface SessionDAO
{
    public List<Session> retrieveSessionList(String userKey) throws GreenplumException;
    public Result killSession (String pid, String userKey) throws GreenplumException;
}
