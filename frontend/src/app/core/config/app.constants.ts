type AppRuntimeConfig = {
  apiBaseUrl: string;
  authTokenKey: string;
};

type RuntimeWindow = typeof globalThis & {
  __QMA_CONFIG__?: Partial<AppRuntimeConfig>;
};

function requireConfigValue(
  value: string | undefined,
  key: keyof AppRuntimeConfig
): string {
  const normalized = value?.trim();

  if (!normalized) {
    throw new Error(`Missing runtime config value: ${key}`);
  }

  return normalized;
}

function readRuntimeConfig(): AppRuntimeConfig {
  const runtimeConfig = (globalThis as RuntimeWindow).__QMA_CONFIG__;

  return {
    apiBaseUrl: requireConfigValue(runtimeConfig?.apiBaseUrl, 'apiBaseUrl'),
    authTokenKey: requireConfigValue(runtimeConfig?.authTokenKey, 'authTokenKey')
  };
}

const runtimeConfig = readRuntimeConfig();

export const APP_API_BASE = runtimeConfig.apiBaseUrl;
export const AUTH_TOKEN_KEY = runtimeConfig.authTokenKey;
