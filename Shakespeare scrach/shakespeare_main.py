# Threading example
from bs4 import BeautifulSoup
import urllib
import os, errno
main_site = 'http://shakespeare.mit.edu/'

# get all works names of shakespeare
# the data structure is {catagory : {poet_name : poet_url}}
def getAllWorksNames():
    r = urllib.urlopen('http://shakespeare.mit.edu/index.html').read()
    soup = BeautifulSoup(r)
    table = soup.find_all('table')[1]
    tr = table.find_all('tr')
    result = {}
    catagory = tr[0]
    for catag in catagory.find_all('td'):
        result[catag.h2.get_text().replace('\n',' ').strip()] = {}

    name = tr[1]
    tmp_result = {}
    for i in range(4):
        tmp_result[i] = {}
    i = 0
    for poets in name.find_all('td'):
        _tmp_result = {}
        for poet in poets.find_all('a'):
            poet_name = poet.get_text().replace('\n',' ').strip()
            _tmp_result[poet_name] = main_site+poet["href"]
        tmp_result[i] = _tmp_result
        i = i+1
    i = 0
    for catag in result:
        result[catag] = tmp_result[i]
        i = i+1

    return result
def makeDirectory(directory):
    if not os.path.exists(directory):
        try:
            os.makedirs(directory)
        except OSError as e:
            if e.errno != errno.EEXIST:
                raise
def downloadScene(f,scene_name,parent_url,scene_url):
    f.write(scene_name)
    f.write('\n')
    parent_url = parent_url.replace("index.html",'')
    scene_url = parent_url + scene_url
    r = urllib.urlopen(scene_url).read()
    soup = BeautifulSoup(r)
    a_group = soup.find_all('a')
    for a in a_group:
        if a.has_attr('name'):
            text = a.get_text()
            if (("speech") in a["name"]) :
                f.write(text.strip())
                f.write('\n')
            else :
                spot = text.find('.') if text.find('.') > 0 else text.find('!')
                spot = spot if spot>0 else text.find('?')
                if (spot > 0):
                    f.write(text[:spot+1].strip())
                    f.write('\n')
                    f.write(text[spot + 1:].strip())
                else:
                    f.write(text)

def downloadWork(directory,work_name,work_url):
    directory = directory+'/'+work_name
    makeDirectory(directory)
    r = urllib.urlopen(work_url).read()
    soup = BeautifulSoup(r)
    paragraphs = soup.find_all('p')[2:]
    actCount = 1
    sceneCount = 1
    for paragraph in paragraphs:
        scenes = paragraph.find_all('a')
        sceneCount = 1
        makeDirectory(directory+'/'+str(actCount))
        for scene in scenes:
            f = open(directory+'/'+str(actCount)+'/'+str(sceneCount), 'w+')
            downloadScene(f,scene.get_text().replace('\n',' ').strip(),work_url,scene["href"].replace('\n',' ').strip())
            f.close()
            sceneCount = sceneCount + 1
        actCount = actCount + 1






if __name__ == "__main__":
    result = getAllWorksNames()
    # for catag in result:
    #     print "result[%s]=" % catag, result[catag]
    directory = os.getcwd()
    directory = directory + '/shakespeare_collection'
    makeDirectory(directory)
    for catalog in result:
        subCatalog_directory = directory+'/'+catalog
        makeDirectory(subCatalog_directory)
        subWorksSet = result[catalog]
        for work in subWorksSet:
            downloadWork(subCatalog_directory,work,subWorksSet[work])

