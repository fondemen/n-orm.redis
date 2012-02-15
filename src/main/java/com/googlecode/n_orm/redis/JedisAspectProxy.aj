import java.lang.reflect.Method;

import org.aspectj.lang.reflect.MethodSignature;

import redis.clients.jedis.Jedis;
import com.googlecode.n_orm.redis.RedisStore;
import com.googlecode.n_orm.storeapi.SimpleStore;

aspect JedisAspectProxy {

	pointcut storeCall(SimpleStore store): call(* *..SimpleStore.*(..)) && target(store);

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
				RedisStore.pool.returnResource(currentJedis);
			} catch (Exception e1) {
				System.err.println("2nd Exception in RedisProxy, trace below, we abort");
				RedisStore.pool.returnBrokenResource(currentJedis);
				e1.printStackTrace();
			}

			
		}
		
		return result;
	}

	
    //Object around(Bank bank): BankCheck(bank) && !within(BankTotalBalanceAspect);

	
	
	Object around(SimpleStore store): storeCall(store) && !within(JedisAspectProxy) {
		Object result;
		System.err.println("<catch call "+thisJoinPointStaticPart.getSignature().getName());

		for(StackTraceElement elt : Thread.currentThread().getStackTrace()) {
			System.err.println(elt.getClassName());	
		}
		
		if(RedisStore.countRead != 0 && RedisStore.countWrite != 0) {
			System.err.println("Erreur, countRead non vide");
			
		}		
		result = proceed(store);


		System.out.println("  <stats lectures='"+RedisStore.countRead+"' \tecritures='"+RedisStore.countWrite+"' />\n");
		System.err.println("</catch call "+thisJoinPointStaticPart.getSignature().getName());


		return result;
	}
	
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