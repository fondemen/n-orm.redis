import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.reflect.MethodSignature;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import com.googlecode.n_orm.redis.RedisStore;

aspect JedisAspectProxy {
	private static Logger logger = Logger.getLogger(RedisStore.class.getName());

	pointcut jedisCall(): call(* *..Jedis.*(..)) && !call(Transaction *..Jedis.multi(..));
	
	Object around(Jedis jedis, RedisStore store): jedisCall() && target(jedis) && this(store) && if(jedis == null) {
		Jedis currentJedis = store.pool.getResource();
		Object result = null;
		
		try {
			result = proceed(currentJedis, store);
			store.pool.returnResource(currentJedis);
		} catch(Exception e) {
			store.pool.returnBrokenResource(currentJedis);
			
			if (e instanceof InvocationTargetException)
				e = (Exception)e.getCause();
			
			logger.log(Level.WARNING, "Exception in RedisProxy caught ; retrying...", e);
			
			Object[] args = thisJoinPoint.getArgs();
			MethodSignature sign = (MethodSignature)thisJoinPointStaticPart.getSignature();

			Method method = sign.getMethod();
			
			currentJedis = store.pool.getResource();
			try {
				result = method.invoke(currentJedis, args);
				store.pool.returnResource(currentJedis);
			} catch (Exception e1) {
				logger.log(Level.WARNING, "2nd Exception in RedisProxy ; aborting", e);
				store.pool.returnBrokenResource(currentJedis);
			}

			
		}
		
		return result;
	}
}