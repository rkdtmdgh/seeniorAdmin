// logger
function createLogger(environment) {
	// 로그 레벨 정의
    const LogLevel = {
        DEBUG: 0,
        INFO: 1,
        WARN: 2,
        ERROR: 3
    };
    
    // 현재 환경에 따라 로그 레벨을 설정
    const isProduction = environment === 'production';
    const currentLogLevel = isProduction ? LogLevel.ERROR : LogLevel.DEBUG;
    
    // 로거 객체 반환
    return {
		log: function(level, ...messages) {
            if (level >= currentLogLevel) { // 배포 환경에 따라 레벨에 맞는 콘솔만 실행
                const timestamp = new Date().toISOString();
                const logMessage = `[${timestamp}]`;
                switch (level) {
                    case LogLevel.DEBUG:
                        console.debug(logMessage, ...messages);
                        break;
                    case LogLevel.INFO:
                        console.info(logMessage, ...messages);
                        break;
                    case LogLevel.WARN:
                        console.warn(logMessage, ...messages);
                        break;
                    case LogLevel.ERROR:
                        console.error(logMessage, ...messages);
                        break;
                }
            }
        },
        
        debug: function(...messages) {
            this.log(LogLevel.DEBUG, ...messages);
        },

        info: function(...messages) {
            this.log(LogLevel.INFO, ...messages);
        },
        
        warn: function(...messages) {
            this.log(LogLevel.WARN, ...messages);
        },

        error: function(...messages) {
            this.log(LogLevel.ERROR, ...messages);
        }
    };
}

// 환경 설정 및 로거 생성
const logger = createLogger('development'); // 개발 환경에서는 'development', 배포 환경에서는 'production'

// 예제 로그 출력
// logger.debug("Debugging data", { key: "value" });
// logger.info("User login", { username: "johndoe" });
// logger.warn("API response", response);
// logger.error("Resource load failed", { resource: "/image.png", errorCode: 404 });
