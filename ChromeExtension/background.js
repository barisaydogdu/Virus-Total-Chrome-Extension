//REPORT LINK Menüye ekleme
chrome.runtime.onInstalled.addListener(() => {
  //Sağ tıklama menüsüne yeni bir öğe ekleme
  chrome.contextMenus.create({
    id: "reportUrl", //Menü öğesi benzersiz kimliği
    title: "Report Risky URL", //Menü öğesi başlığı 
    contexts: ["link"] //Uzantının sadece linkler üzerinde işleme alınması
  });
});

//REPORTLINK FONKSIYON CAĞIRMA
chrome.contextMenus.onClicked.addListener((info, tab) => {
  //Tıklanan öğenin kimliği reportLink ise 
  if (info.menuItemId === "reportUrl") {
    const url = info.linkUrl; //tıklanan bağlantının URL'si alınıyor
    reportUrl(url, tab.id);
  }
});

function reportUrl(url,tabid,)
{
  //URL'de Key-Value yönetimini yapmak için
  const requestBody = new URLSearchParams();

  //parametreden gelen URL'i URLSearchParams'a ekler
  requestBody.append('url', url);
  //Gönderilen istek hata ayıklamak için consola yazdırılır
  console.log('Sending request:', requestBody.toString()); // Hata ayıklama için

  fetch(`http://localhost:8080/scan`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded' //İçerik tipi URL kodlanmış form olarak ayarlanır
    },
    //istek gövdesine URL kodlanmış form verisi eklenir.
    body: requestBody.toString()
  })
  .then(response => {
    //istek başarılı değilse,hata fırlatılır  ve hata mesajı JSON olarak çözülür.
    if (!response.ok) {
      return response.json().then(errData => {
        console.error('Server error data:', errData); // Hata ayıklama için
        throw new Error(`HTTP error! status: ${response.status}, message: ${errData.message}`);
      }).catch(err => {
        // JSON dönüşümünde hata olursa yakalanır
        console.error('Failed to parse server error response:', err);
        throw new Error(`HTTP error! status: ${response.status}, unable to parse error message`);
      });
    }
    //istek başarılı olursa yanıt JSON olarak çözülür
    return response.json();
  })
  .then(data => {
    //istek başarılı olduğunda alınan yanıtı konsola yazdır
    console.log('Received response:', data); // Hata ayıklama için
    showNotificationReportUrl(url, data);
  })
  .catch(error => {
    //hata oluştuğunda,hatayı konsola yazdırır 
    console.error('Error:', error);
    showNotificationReportUrl(url, { response_code: 0, verbose_msg: error.message });
  });
}

function showNotificationReportUrl(url,data)
{
  const title=data.response_code===1? 'İnceleniyor':'Hata';
  const message = data.response_code === 1 
  ? `${url} tarama kuyruğuna eklendi, daha sonra tekrar kontrol edin.` 
  : `Bağlantı taranırken hata oluştu: ${data.verbose_msg}`;
  const iconUrl = data.response_code === 1 ? 'icons/underview1.png' : 'icons/error.png';
  
  chrome.notifications.create('', {
    type: 'basic', // bildirim tipi
    iconUrl: iconUrl, // bildirim ikonu
    title: title, // Bildirim başlığı
    message: message, // bildirim mesajı
    priority: 2 // bildirim önceliği
  });
}

//---------------------------------------------------------------------
//ANALYZ URL
chrome.runtime.onInstalled.addListener(() => {
  //Sağ tıklama menüsüne yeni bir öğe ekleme
  chrome.contextMenus.create({
    id: "analyzeUrl", //Menü öğesi benzersiz kimliği
    title: "Scan for Viruses", //Menü öğesi başlığı 
    contexts: ["link"] //Uzantının sadece linkler üzerinde işleme alınması
  });
});
//ANALYZ URL
//Sağ tıklama menüsündeki bir öğeye tıklandığında çalışacak olay dinleyicisi
chrome.contextMenus.onClicked.addListener((info, tab) => {
  //Tıklanan öğenin kimliği analyzeUrl ise 
  if (info.menuItemId === "analyzeUrl") {
    const url = info.linkUrl; //tıklanan bağlantının URL'si alınıyor
    analyzeUrl(url, tab.id);
  }
});

function analyzeUrl(url, tabId) {
  //Analiz için API'ye bir istek atılıyor
  fetch(`http://localhost:8080/urlReport?url=${encodeURIComponent(url)}`)
    .then(response => response.json()) //API yanıtı JSON formatına dönüştürülür
    .then(data => {
      //Analiz sonuçlarına göre bildirim gösteriliyor
      showNotificationAnalyz(url, data);
    })
    .catch(error => console.error('Error:', error));
}
function showNotificationAnalyz(url, data) {
  const title = data.positives > 0 ? 'Uyarı' : 'Güvenli'; 
  const message = data.positives > 0 ? `${url} zararlı olabilir!` :'Link zararsız görünüyor!' //`${url} zararsız görünüyor.`;
  const iconUrl = data.positives > 0 ? 'icons/dangerlogo.png' : 'icons/safelogo1.png';

  chrome.notifications.create('', {
    type: 'basic', //bildirim tipi
    iconUrl: iconUrl, //bildirim iconu
    title: title, //Bildirim mesajı
    message: message, //bildirim mesajı
    priority: 2 //bildirim önceliği
  });
}
