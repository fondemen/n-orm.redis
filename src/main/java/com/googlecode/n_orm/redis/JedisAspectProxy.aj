import java.lang.reflect.Method;

import org.aspectj.lang.reflect.MethodSignature;

import redis.clients.jedis.Jedis;
import com.googlecode.n_orm.redis.RedisStore;

aspect JedisAspectProxy {

	
	pointcut jedisCall(): call(* *..Jedis.*(..));

	
	Object around(Jedis jedis): jedisCall() && target(jedis) {
		Jedis currentJedis = RedisStore.pool.getResource();
		Object result = null;
		
		Object[] args = thisJoinPoint.getArgs();
		MethodSignature sign = (MethodSignature)thisJoinPointStaticPart.getSignature();

		Method method = sign.getMethod();
		
		try {
			result = method.invoke(currentJedis, args);
			RedisStore.pool.returnResource(currentJedis);
		} catch(Exception e) {
			System.err.println("Exception in RedisProxy caught, stack trace below and we will retry...");
			e.printStackTrace();

			RedisStore.pool.returnBrokenResource(currentJedis);
			currentJedis = RedisStore.pool.getResource();
			try {
				result = method.invoke(currentJedis, args);
			} catch (Exception e1) {
				System.err.println("2nd Exception in RedisProxy, trace below, we abort");
				RedisStore.pool.returnBrokenResource(currentJedis);
				e1.printStackTrace();
			}

			
		}
		
		return result;
	}

}