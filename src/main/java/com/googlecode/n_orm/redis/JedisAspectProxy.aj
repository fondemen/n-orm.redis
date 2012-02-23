import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.reflect.MethodSignature;

import redis.clients.jedis.Jedis;
import com.googlecode.n_orm.redis.RedisStore;
import com.googlecode.n_orm.storeapi.SimpleStore;

aspect JedisAspectProxy {
	private static Logger logger = Logger.getLogger(RedisStore.class.getName());

	pointcut storeCall(SimpleStore store): call(* *..SimpleStore.*(..)) && target(store);

	pointcut jedisCall(): call(* *..Jedis.*(..));
	
	Object around(Jedis jedis, RedisStore store): jedisCall() && target(jedis) && this(store) {
		Jedis currentJedis = store.pool.getResource();
		Object result = null;
		
		Object[] args = thisJoinPoint.getArgs();
		MethodSignature sign = (MethodSignature)thisJoinPointStaticPart.getSignature();

		Method method = sign.getMethod();
		
		try {
			result = method.invoke(currentJedis, args);
			store.pool.returnResource(currentJedis);
		} catch(Exception e) {
			logger.log(Level.WARNING, "Exception in RedisProxy caught ; retrying...", e);

			store.pool.returnBrokenResource(currentJedis);
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

	
    //Object around(Bank bank): BankCheck(bank) && !within(BankTotalBalanceAspect);

	
	
//	Object around(SimpleStore store): storeCall(store) && !within(JedisAspectProxy) {
//		Object result;
//		System.err.println("<catch call "+thisJoinPointStaticPart.getSignature().getName());
//
//		for(StackTraceElement elt : Thread.currentThread().getStackTrace()) {
//			System.err.println(elt.getClassName());	
//		}
//		
//		if(RedisStore.countRead.get() != 0 && RedisStore.countWrite.get() != 0) {
//			System.err.println("Erreur, countRead non vide");
//			
//		}		
//		result = proceed(store);
//
//
//		System.out.println("  <stats lectures='"+RedisStore.countRead+"' \tecritures='"+RedisStore.countWrite+"' />\n");
//		System.err.println("</catch call "+thisJoinPointStaticPart.getSignature().getName());
//
//
//		return result;
//	}
	
	/*
	before(): storeCall() && !within(JedisAspectProxy) {
		MethodSignature sign = (MethodSignature)thisJoinPointStaticPart.getSignature();

		try {
			SimpleStore.class.getMethod(sign.getName(), sign.getParameterTypes());
		} catch(Exception e) {
			return;
		}
		System.out.println("<"+thisJoinPointStaticPart.getSignature().getName()+">");

		if(RedisStore.countRead != 0 && RedisStore.countWrite != 0) {
			System.err.println("Erreur, countRead non vide");
			
		}
		// RedisStore.countWrite = 0;
	}
	
	after(): storeCall() && !within(JedisAspectProxy) {
		MethodSignature sign = (MethodSignature)thisJoinPointStaticPart.getSignature();
		
		try {
			SimpleStore.class.getMethod(sign.getName(), sign.getParameterTypes());
		} catch(Exception e) {
			return;
		}
		
		System.out.println("<stats lectures='"+RedisStore.countRead+"' \tecritures='"+RedisStore.countWrite+"' />\n");
		System.out.print("</"+thisJoinPointStaticPart.getSignature().getName()+">");
		
		RedisStore.countRead = 0;
		RedisStore.countWrite = 0;

	}

*/
}