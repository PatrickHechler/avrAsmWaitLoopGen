package de.hechler.patrick.hilfen.avrasmwait.objects;

import java.math.BigInteger;

public class Loop {
	
	private static final BigInteger[] MAX_ITERATIONS = new BigInteger[] {
		new BigInteger(1, new byte[] { 1 
			,0
			}),
		new BigInteger(1, new byte[] { 1 
			,0,0
		}),
		new BigInteger(1, new byte[] { 1 
			,0,0,0
		}),
		new BigInteger(1, new byte[] { 1 
			,0,0,0,0
		}),
		new BigInteger(1, new byte[] { 1 
			,0,0,0,0,0
		}),
		new BigInteger(1, new byte[] { 1 
			,0,0,0,0,0 ,0
		}),
		new BigInteger(1, new byte[] { 1 
			,0,0,0,0,0 ,0,0
		}),
		new BigInteger(1, new byte[] { 1 
			,0,0,0,0,0 ,0,0,0
		}),
		new BigInteger(1, new byte[] { 1 
			,0,0,0,0,0 ,0,0,0,0
		}),
		new BigInteger(1, new byte[] { 1 
			,0,0,0,0,0 ,0,0,0,0,0
		}),
		new BigInteger(1, new byte[] { 1 
			,0,0,0,0,0 ,0,0,0,0,0
			,0
		}),
		new BigInteger(1, new byte[] { 1 
			,0,0,0,0,0 ,0,0,0,0,0
			,0,0
		}),
		new BigInteger(1, new byte[] { 1 
			,0,0,0,0,0 ,0,0,0,0,0
			,0,0,0
		}),
		new BigInteger(1, new byte[] { 1 
			,0,0,0,0,0 ,0,0,0,0,0
			,0,0,0,0
		}),
		new BigInteger(1, new byte[] { 1 
			,0,0,0,0,0 ,0,0,0,0,0
			,0,0,0,0,0
		}),
		new BigInteger(1, new byte[] { 1 
			,0,0,0,0,0 ,0,0,0,0,0
			,0,0,0,0,0 ,0
		}),
		new BigInteger(1, new byte[] { 1 
			,0,0,0,0,0 ,0,0,0,0,0
			,0,0,0,0,0 ,0,0
		}),
		new BigInteger(1, new byte[] { 1 
			,0,0,0,0,0 ,0,0,0,0,0
			,0,0,0,0,0 ,0,0,0
		}),
		new BigInteger(1, new byte[] { 1 
			,0,0,0,0,0 ,0,0,0,0,0
			,0,0,0,0,0 ,0,0,0,0
		}),
		new BigInteger(1, new byte[] { 1 
			,0,0,0,0,0 ,0,0,0,0,0
			,0,0,0,0,0 ,0,0,0,0,0
		}),
		new BigInteger(1, new byte[] { 1 
			,0,0,0,0,0 ,0,0,0,0,0
			,0,0,0,0,0 ,0,0,0,0,0
			,0
		}),
		new BigInteger(1, new byte[] { 1 
			,0,0,0,0,0 ,0,0,0,0,0
			,0,0,0,0,0 ,0,0,0,0,0
			,0,0
		}),
	};
	
	public static BigInteger maxIterations(int registers) {
		return MAX_ITERATIONS[registers - 1];
	}
	
}
