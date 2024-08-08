// İçerik betiği, kullanıcının seçtiği URL'yi alacak ve API'ye gönderecek
console.log("Content script running...");

chrome.scripting.executeScript({
  target: {tabId: tab.id},
  func: () => {
    const selectedText = window.getSelection().toString().trim();
    if (selectedText !== "") {
      const apiUrl = `http://localhost:8080/urlReport?url=${encodeURIComponent(selectedText)}`;

      fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
          console.log("API Response:", data);
          // API yanıtını burada işleyebilirsiniz
        })
        .catch(error => console.error("API Error:", error));
    }
  }
});
