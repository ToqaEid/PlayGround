<?xml version="1.0" encoding="UTF-8"?>



<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>


    
    <xsl:template match="chat">
        <html>
            <head>
                <meta charset="UTF-8"/>
                <title>Chat Widget</title>
    
    
                <link rel="stylesheet" href="View/reset.css"/>

                <link rel="stylesheet prefetch" href='https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css'/>

                <link rel="stylesheet" href="View/style.css"/>

            </head>
            <body>
                <div class="container clearfix"> 
                    <div class="people-list" id="people-list">
                        <ul class="list">
                            
                            <xsl:for-each select="users/notMe/name">
                                <li class="clearfix">
                                    <img src="https://lh4.ggpht.com/EJNRVilnQU4-mIp2yMmd2MYj5N-R58psCWc9che116OUGlqlswbyF0D_-WL5aZQbmSs=w300" height="42" width="42" alt="avatar" />
                                    <div class="about">
                                        <div class="name">
                                            <xsl:value-of select="text()"/>
                                        </div>                                   
                                    </div>
                                </li>
                            </xsl:for-each>
                                   
                        </ul>
                    </div>
                    <div class="chat">
                        <xsl:variable name="myID" select="users/me/id"/> 
                        <div class="chat-header clearfix">

                            <div class="chat-about">
                                <div class="chat-with">
                                    <xsl:variable name="myName" select="users/me/userName"/> 
                                    you <xsl:value-of select="users/me/userName"/> and : 
                                    
                                    <xsl:for-each select="users/notMe/name">
                                        <xsl:value-of select="text()"/>..
                                    </xsl:for-each>
                                </div>
          
                            </div>
                            <i class="fa fa-star"></i>
                        </div>
                        <div class="chat-history">
                            <ul>
                                <xsl:for-each select="messages/message">
                     
                        
                                    <xsl:variable name="otherID" select="from"/> 
                                    <xsl:variable name="color" select="color"/> 
                                    <xsl:variable name="font" select="font"/> 
                                    <xsl:variable name="senderName" select="senderName"/> 
                                    <xsl:if test="$myID = $otherID">  
                                        
                                        
                                        
                                        
                                        
                                        <li>
                                            <div class="message-data">
                                                <span class="message-data-name">
                                                    <i class="fa fa-circle online"></i>
                                                    <xsl:value-of select="$senderName"/>
                                                </span>
              
                                            </div>
                                            <div class="message my-message">
                                                <font face="{$font}" color="{$color}">
                                                    <xsl:value-of select="text"/>
                                                </font> 
                                            </div>
                                        </li>
                             
                                    </xsl:if> 
                                    <xsl:if test="$myID != $otherID">  
                                        <li class="clearfix">
                                            <div class="message-data align-right">
               
                                                <span class="message-data-name" >
                                                    <xsl:value-of select="$senderName"/>
                                                </span> 
                                                <i class="fa fa-circle me"></i>
              
                                            </div>
                                            <div class="message other-message float-right">
                                                <font face="{$font}" color="{$color}">
                                                    <xsl:value-of select="text"/>
                                                </font> 
                                            </div>
                                        </li>
                                
                             
                                    </xsl:if> 
                                    <br/> 
                                </xsl:for-each>
                            </ul>
        
                        </div>
                    </div>
                </div>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
