<!--

   Derby - Class advancedViewXSL2

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 -->
<!DOCTYPE stylesheet [
        <!ENTITY space "<xsl:text> </xsl:text>">
        <!ENTITY cr "<xsl:text>
</xsl:text>">
        ]>

<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        version="1.0">
    <!-- Designed & coded by C.S.Nirmal J. Fernando, of University of Moratuwa, Sri Lanka -->
    <xsl:output method="html" indent="yes"
                doctype-public="-//W3C//DTD HTML 4.01//EN"
                doctype-system="http://www.w3.org/TR/html4/strict.dtd"/>

    <xsl:strip-space elements="*"/>
    <xsl:output omit-xml-declaration="yes"/>
    <xsl:template match="/">
        <H3>Executed Date &amp; Time: <font color="#4E9258"> <xsl:value-of select="//time"/> </font>
            STMT_ID: <font color="#4E9258"> <xsl:value-of select="//stmt_id"/> </font></H3>
        <ul id="main-ul">
            <xsl:for-each select="root/plan">
                <xsl:if test="count(member)!=0">
                    <h3>Node: <font color="#119911"> <xsl:apply-templates select="member"/> </font>
                        <xsl:if test="count(elapsed_time)!=0">
                            elapsed_time: <font color="#119911"> <xsl:apply-templates select="elapsed_time"/> </font>
                        </xsl:if>
                        <xsl:if test="count(begin_exe_time)!=0">
                            begin_exe_time: <font color="#119911"> <xsl:apply-templates select="begin_exe_time"/> </font>
                        </xsl:if>
                        <xsl:if test="count(end_exe_time)!=0">
                            end_exe_time: <font color="#119911"> <xsl:apply-templates select="end_exe_time"/> </font>
                        </xsl:if>

                    </h3>
                </xsl:if>
                <!--xsl:if test="count(statement)!=0">
                    <h4>SQL: <font color="#119911">
                    <xsl:apply-templates select="statement"/>
                    </font></h4>
                </xsl:if-->

                <xsl:for-each select="details">
                    <xsl:apply-templates >
                        <xsl:with-param name="i" select="0"/>
                    </xsl:apply-templates>
                </xsl:for-each>

                <xsl:if test="count(local/member)!=0">
                    <h3>Node: <font color="#119911"> <xsl:apply-templates select="local/member"/> </font>
                        <xsl:if test="count(local/elapsed_time)!=0">
                            elapsed_time: <font color="#119911"><xsl:apply-templates select="local/elapsed_time"/> </font>
                        </xsl:if>
                        <xsl:if test="count(local/begin_exe_time)!=0">
                            begin_exe_time: <font color="#119911"> <xsl:apply-templates select="local/begin_exe_time"/> </font>
                        </xsl:if>
                        <xsl:if test="count(local/end_exe_time)!=0">
                            end_exe_time: <font color="#119911"> <xsl:apply-templates select="local/end_exe_time"/> </font>
                        </xsl:if>
                    </h3>
                </xsl:if>
                <!--xsl:if test="count(local/statement)!=0">
                    <h4>SQL: <font color="#119911">
                    <xsl:apply-templates select="local/statement"/>
                    </font></h4>
                </xsl:if-->

                <xsl:for-each select="local/details">
                    <xsl:apply-templates >
                        <xsl:with-param name="i" select="0"/>
                    </xsl:apply-templates>
                </xsl:for-each>
            </xsl:for-each>
        </ul>
        <H1> <font align="center"> END OF PLAN </font> </H1>
        <H1> <font color="transparent"> ---------------------- </font> </H1>
        <H1> <font color="transparent"> ---------------------- </font> </H1>
        <H1> <font color="transparent"> ---------------------- </font> </H1>
        <H1> <font color="transparent"> ---------------------- </font> </H1>
        <H1> <font color="transparent"> ---------------------- </font> </H1>
        <H1> <font color="transparent"> ---------------------- </font> </H1>
        <H1> <font color="transparent"> ---------------------- </font> </H1>
        <H1> <font color="transparent"> ---------------------- </font> </H1>
        <H1> <font color="transparent"> ---------------------- </font> </H1>
        <H1> <font color="transparent"> ---------------------- </font> </H1>
        <H1> <font color="transparent"> ---------------------- </font> </H1>
        <H1> <font color="transparent"> ---------------------- </font> </H1>
    </xsl:template>

    <xsl:template match="details">
        <xsl:for-each select="node">
            <xsl:apply-templates/>
        </xsl:for-each>
        <xsl:for-each select="node/@*">
            <xsl:apply-templates/>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="node">
        <xsl:param name="i"/>
        <li>
            <span class="collapse" onclick="toggle(this);"  ondblclick="Collapser(this);">
                <xsl:if test="$i!=0">
                    |_
                </xsl:if>
                <xsl:value-of select="@name"/>
                <xsl:if test="@rank=1">
                    <font color="red">*</font>
                </xsl:if>
                <xsl:if test="@rank=2">
                    <font color="brown">*</font>
                </xsl:if>&space;
                <xsl:if test="@member_node">
                    <xsl:value-of select="@member_node"/>
                </xsl:if>
                <br></br>
            </span>
            <table>
                <xsl:if test="count(@rs_name)!=0"><tr>
                    <xsl:apply-templates select="@rs_name"/>
                </tr></xsl:if>
                <xsl:if test="count(@no_children)!=0"><tr>
                    <xsl:apply-templates select="@no_children"/>
                </tr></xsl:if>
                <xsl:if test="count(@execute_time)!=0"><tr>
                    <xsl:apply-templates select="@execute_time"/>
                </tr></xsl:if>
                <xsl:if test="count(@percent_exec_time)!=0"><tr>
                    <xsl:apply-templates select="@percent_exec_time"/>
                </tr></xsl:if>
                <xsl:if test="count(@node_details)!=0"><tr>
                    <xsl:apply-templates select="@node_details"/>
                </tr></xsl:if>
                <xsl:if test="count(@ser_deser_time)!=0"><tr>
                    <xsl:apply-templates select="@ser_deser_time"/>
                </tr></xsl:if>
                <xsl:if test="count(@process_time)!=0"><tr>
                    <xsl:apply-templates select="@process_time"/>
                </tr></xsl:if>
                <xsl:if test="count(@throttle_time)!=0"><tr>
                    <xsl:apply-templates select="@throttle_time"/>
                </tr></xsl:if>
                <xsl:if test="count(@construct_time)!=0"><tr>
                    <xsl:apply-templates select="@construct_time"/>
                </tr></xsl:if>
                <xsl:if test="count(@open_time)!=0"><tr>
                    <xsl:apply-templates select="@open_time"/>
                </tr></xsl:if>
                <xsl:if test="count(@next_time)!=0"><tr>
                    <xsl:apply-templates select="@next_time"/>
                </tr></xsl:if>
                <xsl:if test="count(@close_time)!=0"><tr>
                    <xsl:apply-templates select="@close_time"/>
                </tr></xsl:if>
                <xsl:if test="count(@avg_next_time_per_row)!=0"><tr>
                    <xsl:apply-templates select="@avg_next_time_per_row"/>
                </tr></xsl:if>
                <xsl:if test="count(@percent_exec_time)!=0"><tr>
                    <xsl:apply-templates select="@percent_exec_time"/>
                </tr></xsl:if>
                <xsl:if test="count(@input_rows)!=0"><tr>
                    <xsl:apply-templates select="@input_rows"/>
                </tr></xsl:if>
                <xsl:if test="count(@returned_rows)!=0"><tr>
                    <xsl:apply-templates select="@returned_rows"/>
                </tr></xsl:if>
                <xsl:if test="count(@no_opens)!=0"><tr>
                    <xsl:apply-templates select="@no_opens"/>
                </tr></xsl:if>
                <xsl:if test="count(@visited_pages)!=0"><tr>
                    <xsl:apply-templates select="@visited_pages"/>
                </tr></xsl:if>
                <xsl:if test="count(@visited_rows)!=0"><tr>
                    <xsl:apply-templates select="@visited_rows"/>
                </tr></xsl:if>
                <xsl:if test="count(@scan_qualifiers)!=0"><tr>
                    <xsl:apply-templates select="@scan_qualifiers"/>
                </tr></xsl:if>
                <xsl:if test="count(@next_qualifiers)!=0"><tr>
                    <xsl:apply-templates select="@next_qualifiers"/>
                </tr></xsl:if>
                <xsl:if test="count(@scanned_object)!=0"><tr>
                    <xsl:apply-templates select="@scanned_object"/>
                </tr></xsl:if>
                <xsl:if test="count(@scan_type)!=0"><tr>
                    <xsl:apply-templates select="@scan_type"/>
                </tr></xsl:if>
                <xsl:if test="count(@sort_type)!=0"><tr>
                    <xsl:apply-templates select="@sort_type"/>
                </tr></xsl:if>
                <xsl:if test="count(@sorter_output)!=0"><tr>
                    <xsl:apply-templates select="@sorter_output"/>
                </tr></xsl:if>
                <xsl:if test="count(@sorter_input)!=0"><tr>
                    <xsl:apply-templates select="@sorter_input"/>
                </tr></xsl:if>
            </table>
            <ul>
                <br></br>
                <xsl:apply-templates select="node"/>
            </ul>
        </li>
    </xsl:template>

    <xsl:template match="node/@*">
        <th align="left">
            <xsl:value-of select="name()"/>
        </th>
        <td>
            <xsl:value-of select="."/>
        </td>
    </xsl:template>

</xsl:stylesheet>
