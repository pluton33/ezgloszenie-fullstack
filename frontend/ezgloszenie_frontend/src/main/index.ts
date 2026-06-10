import { app, shell, BrowserWindow, ipcMain, protocol, net } from 'electron'
import { join } from 'path'
import { electronApp, optimizer, is } from '@electron-toolkit/utils'
import icon from '../../resources/icon.png?asset'

// 1. Rejestracja naszego protokołu
protocol.registerSchemesAsPrivileged([
  { scheme: 'app', privileges: { secure: true, standard: true, supportFetchAPI: true } }
])

function createWindow(): void {
  const mainWindow = new BrowserWindow({
    width: 900,
    height: 670,
    show: false,
    frame: false,
    autoHideMenuBar: true,
    ...(process.platform === 'linux' ? { icon } : {}),
    webPreferences: {
      preload: join(__dirname, '../preload/index.js'),
      sandbox: false
    }
  })

  mainWindow.on('ready-to-show', () => {
    mainWindow.show()
  })

  mainWindow.webContents.setWindowOpenHandler((details) => {
    shell.openExternal(details.url)
    return { action: 'deny' }
  })

  if (is.dev && process.env['ELECTRON_RENDERER_URL']) {
    mainWindow.loadURL(process.env['ELECTRON_RENDERER_URL'])
  } else {
    mainWindow.loadURL('app://-/index.html')
  }
}

app.whenReady().then(() => {
  // 2. NASZE PROXY: Przechwytujemy zapytania Electrona
  protocol.handle('app', async (request) => {
    const url = new URL(request.url);

    // JEŚLI TO ZAPYTANIE DO BACKENDU (Zaczyna się od /api/)
    if (url.pathname.startsWith('/api/')) {
      // Usuwamy /api i doklejamy IP Twojego Spring Boota
      const targetPath = url.pathname.replace(/^\/api/, '');
      const targetUrl = `http://34.116.134.38:8080${targetPath}${url.search}`;

      // Budujemy konfigurację żądania do przekazania
      const options: RequestInit = {
        method: request.method,
        headers: request.headers,
      };

      // Kopiujemy payload (np. dane logowania) dla POST/PUT
      if (request.method !== 'GET' && request.method !== 'HEAD') {
        options.body = await request.arrayBuffer();
      }

      // Główny proces wykonuje fetch - to omija blokady CORS i zachowuje sesję JSESSIONID!
      return net.fetch(targetUrl, options);
    }

    // JEŚLI TO PLIKI REACTA (Działanie domyślne)
    let filePath = url.pathname;
    if (filePath === '/' || filePath === '') filePath = '/index.html';
    return net.fetch('file://' + join(__dirname, '../renderer', filePath));
  })

  electronApp.setAppUserModelId('com.electron')

  app.on('browser-window-created', (_, window) => {
    optimizer.watchWindowShortcuts(window)
  })

  // IPC handlers
  ipcMain.on('ping', () => console.log('pong'))
  ipcMain.on('close-window', (event) => {
    const win = BrowserWindow.fromWebContents(event.sender)
    if (win) win.close()
  })
  ipcMain.on('minimize-window', (event) => {
    const win = BrowserWindow.fromWebContents(event.sender)
    if (win) win.minimize()
  })
  ipcMain.on('maximize-window', (event) => {
    const win = BrowserWindow.fromWebContents(event.sender)
    if (win) {
      if (win.isMaximized()) win.unmaximize()
      else win.maximize()
    }
  })

  createWindow()

  app.on('activate', function () {
    if (BrowserWindow.getAllWindows().length === 0) createWindow()
  })
})

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') app.quit()
})